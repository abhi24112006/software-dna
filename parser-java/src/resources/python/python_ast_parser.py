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

        "name": node.name,

        "returnType":
            annotation_name(node.returns),

        "parameters":
            parameters,

        "calls":
            calls
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