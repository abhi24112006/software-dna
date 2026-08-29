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
                extractAssignments(body)
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