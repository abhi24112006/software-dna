const fs = require("fs");
const path = require("path");

const root = process.argv[2];

if (!root) {
    console.error(
        "Usage: node javascript_ast_parser.js <repository>"
    );
    process.exit(1);
}


/*
 * ============================================================
 * FILE COLLECTION
 * ============================================================
 */

function collectFiles(directory) {

    const result = [];

    for (const entry of fs.readdirSync(directory, {
        withFileTypes: true
    })) {

        const fullPath =
            path.join(directory, entry.name);

        if (entry.isDirectory()) {

            result.push(
                ...collectFiles(fullPath)
            );

        } else if (
            entry.isFile() &&
            (
                entry.name.endsWith(".js") ||
                entry.name.endsWith(".jsx") ||
                entry.name.endsWith(".ts") ||
                entry.name.endsWith(".tsx")
            )
        ) {

            result.push(fullPath);
        }
    }

    return result;
}


/*
 * ============================================================
 * IMPORT EXTRACTION
 * ============================================================
 */

function extractImports(source) {

    const imports = [];

    /*
     * ES module imports:
     *
     * import User from "../model/User.js";
     * import "../config.js";
     */
    const importRegex =
        /import\s+(?:[\s\S]*?\s+from\s+)?["']([^"']+)["']/g;

    let match;

    while (
        (match = importRegex.exec(source)) !== null
    ) {

        imports.push(match[1]);
    }


    /*
     * CommonJS:
     *
     * require("../model/User");
     */
    const requireRegex =
        /require\s*\(\s*["']([^"']+)["']\s*\)/g;

    while (
        (match = requireRegex.exec(source)) !== null
    ) {

        imports.push(match[1]);
    }

    return imports;
}


/*
 * ============================================================
 * CLASS EXTRACTION
 * ============================================================
 */

function extractClasses(source) {

    const classes = [];

    const classRegex =
        /class\s+([A-Za-z_$][\w$]*)\s*(?:extends\s+([A-Za-z_$][\w$]*))?\s*\{/g;

    let match;

    while (
        (match = classRegex.exec(source)) !== null
    ) {

        const className =
            match[1];

        const superClass =
            match[2] || null;

        const bodyStart =
            match.index +
            match[0].length -
            1;

        const bodyEnd =
            findMatchingBrace(
                source,
                bodyStart
            );

        const body =
            bodyEnd >= 0
                ? source.substring(
                    bodyStart + 1,
                    bodyEnd
                )
                : source.substring(
                    bodyStart + 1
                );

        classes.push({

            name: className,

            superClasses:
                superClass
                    ? [superClass]
                    : [],

            methods:
                extractMethods(body),

            fields: []
        });
    }

    return classes;
}


/*
 * ============================================================
 * METHOD EXTRACTION
 * ============================================================
 */

function extractMethods(classBody) {

    const methods = [];

    /*
     * Handles:
     *
     * constructor(...)
     * create(...)
     * async create(...)
     */
    const methodRegex =
        /(?:async\s+)?([A-Za-z_$][\w$]*)\s*\(([^)]*)\)\s*\{/g;

    let match;

    while (
        (match = methodRegex.exec(classBody)) !== null
    ) {

        const methodName =
            match[1];

        const parameterText =
            match[2].trim();

        const parameters =
            parameterText.length === 0
                ? []
                : parseParameters(
                    parameterText
                );

        const bodyStart =
            match.index +
            match[0].length -
            1;

        const bodyEnd =
            findMatchingBrace(
                classBody,
                bodyStart
            );

        const body =
            bodyEnd >= 0
                ? classBody.substring(
                    bodyStart + 1,
                    bodyEnd
                )
                : "";

        methods.push({

            name:
                methodName,

            returnType:
                null,

            parameters:
                parameters,

            calls:
                extractCalls(body),

            assignments:
                extractAssignments(body),

            metrics:
                calculateMethodMetrics(
                    body,
                    parameters
                )
        });
    }

    return methods;
}


/*
 * ============================================================
 * PARAMETER EXTRACTION
 * ============================================================
 */

function parseParameters(parameterText) {

    return parameterText
        .split(",")
        .map(parameter => {

            const cleaned =
                parameter.trim();

            if (!cleaned) {
                return null;
            }

            /*
             * TypeScript:
             *
             * service: UserService
             */
            const parts =
                cleaned.split(":");

            return {

                name:
                    parts[0]
                        .trim()
                        .replace(
                            /[?]/g,
                            ""
                        ),

                type:
                    parts.length > 1
                        ? parts
                            .slice(1)
                            .join(":")
                            .trim()
                        : null
            };

        })
        .filter(
            parameter =>
                parameter !== null
        );
}


/*
 * ============================================================
 * METHOD CALL EXTRACTION
 * ============================================================
 */

function extractCalls(body) {

    const calls = [];

    /*
     * Detects:
     *
     * this.service.createUser()
     * this.repository.save()
     * service.createUser()
     * repository.save()
     *
     * Also supports:
     *
     * this.foo.bar()
     */
    const callRegex =
        /([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*\.\s*([A-Za-z_$][\w$]*)\s*\(/g;

    let match;

    while (
        (match = callRegex.exec(body)) !== null
    ) {

        calls.push({

            receiver:
                match[1],

            method:
                match[2]
        });
    }

    return calls;
}


/*
 * ============================================================
 * ASSIGNMENT EXTRACTION
 * ============================================================
 *
 * Detects dependency assignments such as:
 *
 * this.service = service;
 * this.repository = repository;
 *
 * service = new UserService();
 *
 * this.service = new UserService();
 *
 * These assignments are important because plain JavaScript
 * usually has no parameter type information.
 */

function extractAssignments(body) {

    const assignments = [];

    /*
     * Example:
     *
     * this.service = service;
     *
     * target  = this.service
     * source  = service
     */
    const parameterAssignmentRegex =
        /([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*=\s*([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*;/g;

    let match;

    while (
        (match =
            parameterAssignmentRegex.exec(body))
            !== null
    ) {

        assignments.push({

            target:
                match[1],

            source:
                match[2],

            valueType:
                null
        });
    }


    /*
     * Example:
     *
     * this.service = new UserService();
     *
     * target    = this.service
     * source    = null
     * valueType = UserService
     */
    const constructorAssignmentRegex =
        /([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*=\s*new\s+([A-Za-z_$][\w$]*)\s*\(/g;

    while (
        (match =
            constructorAssignmentRegex.exec(body))
            !== null
    ) {

        assignments.push({

            target:
                match[1],

            source:
                null,

            valueType:
                match[2]
        });
    }

    return assignments;
}


/*
 * ============================================================
 * METHOD METRICS
 * ============================================================
 *
 * These metrics use the common Software DNA metric names.
 *
 * The JavaScript parser is responsible only for calculating
 * language-specific source metrics. The Java adapter will
 * later convert these values into the common MethodMetrics
 * Java model.
 */


/*
 * ------------------------------------------------------------
 * LOCAL VARIABLES
 * ------------------------------------------------------------
 *
 * Detect:
 *
 * let user = ...
 * const user = ...
 * var user = ...
 *
 * Multiple declarations are counted separately:
 *
 * const a = 1, b = 2;
 *
 * -> 2 local variables
 */
function countLocalVariables(body) {

    let count = 0;

    const declarationRegex =
        /\b(?:let|const|var)\s+([^;]+)/g;

    let match;

    while (
        (match =
            declarationRegex.exec(body))
            !== null
    ) {

        const declaration =
            match[1];

        count +=
            declaration
                .split(",")
                .length;
    }

    return count;
}


/*
 * ------------------------------------------------------------
 * METHOD CALL COUNT
 * ------------------------------------------------------------
 *
 * Counts call expressions such as:
 *
 * service.createUser()
 * console.log()
 * save()
 *
 * Constructor calls using "new" are excluded here and are
 * counted separately as object creations.
 */
function countMethodCalls(body) {

    const withoutConstructors =
        body.replace(
            /\bnew\s+[A-Za-z_$][\w$]*(?:\s*<[^>]+>)?\s*\(/g,
            ""
        );

    /*
     * Control-flow constructs are not method calls.
     *
     * Examples:
     *
     * if (...)
     * for (...)
     * while (...)
     * switch (...)
     * catch (...)
     */
    const withoutControlFlow =
        withoutConstructors.replace(
            /\b(?:if|for|while|switch|catch|with)\s*\(/g,
            ""
        );

    const callRegex =
        /\b[A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*\s*\(/g;

    const matches =
        withoutControlFlow.match(
            callRegex
        );

    return matches
        ? matches.length
        : 0;
}


/*
 * ------------------------------------------------------------
 * OBJECT CREATION COUNT
 * ------------------------------------------------------------
 *
 * JavaScript uses:
 *
 * new User()
 * new UserService()
 */
function countObjectCreations(body) {

    const matches =
        body.match(
            /\bnew\s+[A-Za-z_$][\w$]*(?:\s*<[^>]+>)?\s*\(/g
        );

    return matches
        ? matches.length
        : 0;
}


/*
 * ------------------------------------------------------------
 * RETURN COUNT
 * ------------------------------------------------------------
 */
function countReturns(body) {

    const matches =
        body.match(
            /\breturn\b/g
        );

    return matches
        ? matches.length
        : 0;
}


/*
 * ------------------------------------------------------------
 * LOOP COUNT
 * ------------------------------------------------------------
 *
 * Detect:
 *
 * for
 * while
 * do
 */
function countLoops(body) {

    const matches =
        body.match(
            /\b(?:for|while|do)\b/g
        );

    return matches
        ? matches.length
        : 0;
}


/*
 * ------------------------------------------------------------
 * CONDITIONAL COUNT
 * ------------------------------------------------------------
 *
 * Detect:
 *
 * if
 * else if
 * switch
 * case
 * ternary expressions
 */
function countConditionals(body) {

    let count = 0;

    const keywordMatches =
        body.match(
            /\b(?:if|else\s+if|switch|case)\b/g
        );

    if (keywordMatches) {
        count += keywordMatches.length;
    }

    const ternaryMatches =
        body.match(
            /\?/g
        );

    if (ternaryMatches) {
        count += ternaryMatches.length;
    }

    return count;
}


/*
 * ------------------------------------------------------------
 * CYCLOMATIC COMPLEXITY
 * ------------------------------------------------------------
 *
 * Base complexity = 1.
 *
 * Add one for each decision point:
 *
 * if
 * else if
 * for
 * while
 * case
 * catch
 * &&
 * ||
 * ternary
 */
function calculateCyclomaticComplexity(body) {

    let complexity = 1;

    const decisionMatches =
        body.match(
            /\b(?:if|else\s+if|for|while|case|catch)\b/g
        );

    if (decisionMatches) {
        complexity +=
            decisionMatches.length;
    }

    const logicalMatches =
        body.match(
            /&&|\|\|/g
        );

    if (logicalMatches) {
        complexity +=
            logicalMatches.length;
    }

    const ternaryMatches =
        body.match(
            /\?/g
        );

    if (ternaryMatches) {
        complexity +=
            ternaryMatches.length;
    }

    return complexity;
}


/*
 * ------------------------------------------------------------
 * MAXIMUM NESTING DEPTH
 * ------------------------------------------------------------
 *
 * Braces are used as a lightweight approximation for
 * JavaScript block nesting.
 *
 * The method body itself starts at depth 0.
 */
function calculateMaximumNestingDepth(body) {

    let currentDepth = 0;
    let maximumDepth = 0;

    for (
        let i = 0;
        i < body.length;
        i++
    ) {

        if (body[i] === "{") {

            currentDepth++;

            if (
                currentDepth >
                maximumDepth
            ) {

                maximumDepth =
                    currentDepth;
            }

        } else if (
            body[i] === "}"
        ) {

            currentDepth--;

            if (currentDepth < 0) {
                currentDepth = 0;
            }
        }
    }

    return maximumDepth;
}


/*
 * ------------------------------------------------------------
 * LINES OF CODE
 * ------------------------------------------------------------
 *
 * Counts physical lines in the extracted method body.
 *
 * Empty lines are ignored.
 */
function calculateLinesOfCode(body) {

    const lines =
        body.split(/\r?\n/);

    return lines.filter(
        line =>
            line.trim().length > 0
    ).length;
}


/*
 * ------------------------------------------------------------
 * COMMON METHOD METRICS OBJECT
 * ------------------------------------------------------------
 */
function calculateMethodMetrics(
    body,
    parameters
) {

    return {

        linesOfCode:
            calculateLinesOfCode(
                body
            ),

        parameterCount:
            parameters.length,

        localVariableCount:
            countLocalVariables(
                body
            ),

        methodCallCount:
            countMethodCalls(
                body
            ),

        objectCreationCount:
            countObjectCreations(
                body
            ),

        returnCount:
            countReturns(
                body
            ),

        cyclomaticComplexity:
            calculateCyclomaticComplexity(
                body
            ),

        maximumNestingDepth:
            calculateMaximumNestingDepth(
                body
            ),

        loopCount:
            countLoops(
                body
            ),

        conditionalCount:
            countConditionals(
                body
            )
    };
}


/*
 * ============================================================
 * BRACE MATCHING
 * ============================================================
 */

function findMatchingBrace(
    source,
    openingIndex
) {

    let depth = 0;

    for (
        let i = openingIndex;
        i < source.length;
        i++
    ) {

        if (source[i] === "{") {

            depth++;

        } else if (
            source[i] === "}"
        ) {

            depth--;

            if (depth === 0) {

                return i;
            }
        }
    }

    return -1;
}


/*
 * ============================================================
 * FILE PARSING
 * ============================================================
 */

function parseFile(filePath) {

    const source =
        fs.readFileSync(
            filePath,
            "utf8"
        );

    return {

        file:
            filePath,

        classes:
            extractClasses(source),

        imports:
            extractImports(source)
    };
}


/*
 * ============================================================
 * MAIN
 * ============================================================
 */

const files =
    collectFiles(
        path.resolve(root)
    );

const result =
    files.map(
        file =>
            parseFile(file)
    );

console.log(
    JSON.stringify(
        result,
        null,
        2
    )
);