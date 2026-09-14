import ast
import json
import sys
from pathlib import Path


def annotation_name(node):

    if node is None:
        return None

    if isinstance(node, ast.Name):
        return node.id

    if isinstance(node, ast.Attribute):

        parts = []
        current = node

        while isinstance(current, ast.Attribute):
            parts.append(current.attr)
            current = current.value

        if isinstance(current, ast.Name):
            parts.append(current.id)

        return ".".join(reversed(parts))

    if isinstance(node, ast.Subscript):
        return annotation_name(node.value)

    if isinstance(node, ast.Constant):
        return str(node.value)

    return None


def call_info(node):

    if isinstance(node.func, ast.Name):

        return {
            "receiver": None,
            "method": node.func.id
        }

    if isinstance(node.func, ast.Attribute):

        receiver = None

        if isinstance(node.func.value, ast.Name):

            receiver = node.func.value.id

        elif isinstance(node.func.value, ast.Attribute):

            receiver = annotation_name(
                node.func.value
            )

        return {
            "receiver": receiver,
            "method": node.func.attr
        }

    return None


def count_local_variables(node):

    count = 0

    for child in ast.walk(node):

        if isinstance(child, ast.Assign):

            for target in child.targets:

                if isinstance(target, (ast.Name, ast.Tuple, ast.List)):
                    count += count_assignment_targets(target)

        elif isinstance(child, ast.AnnAssign):

            count += count_assignment_targets(
                child.target
            )

        elif isinstance(child, ast.NamedExpr):

            count += 1

    return count


def count_assignment_targets(target):

    if isinstance(target, ast.Name):
        return 1

    if isinstance(target, (ast.Tuple, ast.List)):

        return sum(
            count_assignment_targets(element)
            for element in target.elts
        )

    return 0


def count_method_calls(node):

    return sum(
        1
        for child in ast.walk(node)
        if isinstance(child, ast.Call)
    )


def count_object_creations(node):

    return 0


def count_returns(node):

    return sum(
        1
        for child in ast.walk(node)
        if isinstance(child, ast.Return)
    )


def count_loops(node):

    return sum(
        1
        for child in ast.walk(node)
        if isinstance(
            child,
            (
                ast.For,
                ast.AsyncFor,
                ast.While
            )
        )
    )


def count_conditionals(node):

    count = 0

    for child in ast.walk(node):

        if isinstance(child, ast.If):
            count += 1

        elif isinstance(child, ast.IfExp):
            count += 1

    return count


def calculate_cyclomatic_complexity(node):

    complexity = 1

    for child in ast.walk(node):

        if isinstance(
            child,
            (
                ast.If,
                ast.IfExp,
                ast.For,
                ast.AsyncFor,
                ast.While,
                ast.ExceptHandler
            )
        ):
            complexity += 1

        elif isinstance(child, ast.BoolOp):

            if isinstance(
                child.op,
                (
                    ast.And,
                    ast.Or
                )
            ):
                complexity += max(
                    0,
                    len(child.values) - 1
                )

    return complexity


def is_nesting_node(node):

    return isinstance(
        node,
        (
            ast.If,
            ast.For,
            ast.AsyncFor,
            ast.While,
            ast.Try,
            ast.ExceptHandler,
            ast.With,
            ast.AsyncWith
        )
    )


def calculate_maximum_nesting_depth(node):

    def calculate(current, depth):

        max_depth = depth

        for child in ast.iter_child_nodes(current):

            next_depth = depth

            if is_nesting_node(child):
                next_depth += 1

            max_depth = max(
                max_depth,
                calculate(
                    child,
                    next_depth
                )
            )

        return max_depth

    return calculate(node, 0)


def calculate_lines_of_code(node):

    if (
        not hasattr(node, "lineno")
        or not hasattr(node, "end_lineno")
    ):
        return 0

    return (
        node.end_lineno
        - node.lineno
        + 1
    )


def count_parameters(node):

    parameters = node.args.args

    if (
        parameters
        and parameters[0].arg == "self"
    ):
        return len(parameters) - 1

    return len(parameters)


def method_metrics(node):

    return {
        "linesOfCode":
            calculate_lines_of_code(node),

        "parameterCount":
            count_parameters(node),

        "localVariableCount":
            count_local_variables(node),

        "methodCallCount":
            count_method_calls(node),

        "objectCreationCount":
            count_object_creations(node),

        "returnCount":
            count_returns(node),

        "cyclomaticComplexity":
            calculate_cyclomatic_complexity(node),

        "maximumNestingDepth":
            calculate_maximum_nesting_depth(node),

        "loopCount":
            count_loops(node),

        "conditionalCount":
            count_conditionals(node)
    }


def function_info(node):

    parameters = []

    for arg in node.args.args:

        parameters.append({
            "name": arg.arg,
            "type": annotation_name(arg.annotation)
        })

    calls = []

    for child in ast.walk(node):

        if isinstance(child, ast.Call):

            call = call_info(child)

            if call is not None:

                calls.append(call)

    return {

        "name":
            node.name,

        "returnType":
            annotation_name(node.returns),

        "parameters":
            parameters,

        "calls":
            calls,

        "metrics":
            method_metrics(node)
    }


def class_info(node):

    methods = []

    fields = []

    super_classes = []

    for base in node.bases:

        name = annotation_name(base)

        if name:
            super_classes.append(name)

    for item in node.body:

        if isinstance(
            item,
            (
                ast.FunctionDef,
                ast.AsyncFunctionDef
            )
        ):

            methods.append(
                function_info(item)
            )

        elif isinstance(
            item,
            ast.AnnAssign
        ):

            if isinstance(
                item.target,
                ast.Name
            ):

                fields.append({

                    "name":
                        item.target.id,

                    "type":
                        annotation_name(
                            item.annotation
                        )
                })

    return {

        "name":
            node.name,

        "superClasses":
            super_classes,

        "methods":
            methods,

        "fields":
            fields
    }


def parse_file(file_path):

    source = Path(
        file_path
    ).read_text(
        encoding="utf-8"
    )

    tree = ast.parse(source)

    classes = []

    imports = []

    for node in tree.body:

        if isinstance(
            node,
            ast.ClassDef
        ):

            classes.append(
                class_info(node)
            )

        elif isinstance(
            node,
            ast.Import
        ):

            for alias in node.names:

                imports.append(
                    alias.name
                )

        elif isinstance(
            node,
            ast.ImportFrom
        ):

            if node.module:

                imports.append(
                    node.module
                )

    return {

        "file":
            str(file_path),

        "classes":
            classes,

        "imports":
            imports
    }


def main():

    repository = sys.argv[1]

    result = []

    root = Path(
        repository
    )

    for file in root.rglob(
        "*.py"
    ):

        if file.name == "__init__.py":
            continue

        try:

            result.append(
                parse_file(file)
            )

        except Exception as e:

            result.append({

                "file":
                    str(file),

                "classes": [],

                "imports": [],

                "error":
                    str(e)
            })

    print(
        json.dumps(
            result,
            indent=2
        )
    )


if __name__ == "__main__":
    main()