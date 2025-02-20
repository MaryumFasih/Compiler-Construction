# Roman Urdu Programming Language - README

## Introduction
The Roman Urdu Programming Language is a simple, custom programming language designed for easy readability and understanding, using Roman Urdu keywords. It provides fundamental programming, including arithmetic operations, variable handling, input/output operations, and comments.

This language has a well-structured syntax with clear definitions for keywords, data types, and operations, making coding straightforward and easy to understand.

## Features
- **Arithmetic Operators**: Supports standard operators (`+`, `-`, `*`, `/`, `%`, `=`).
- **Variable Naming**: Must follow the pattern `[a-z][a-z0-9]*` (only lowercase letters and digits).
- **Supports both Global and Local Variables.**
- **Comments**:
  - Single-line: `~~` (e.g., `~~ This is a comment`)
  - Multi-line: `-% ... %-` (e.g., `-% This is a multi-line comment %-`)
- **Input/Output Functions**:
  - `lo` → Input
  - `do` → Output
  - `likho` → Print
- **Data Types**:
  - `jhanda` → Boolean
  - `adad` → Integer
  - `asharia` → Float/Decimal
  - `harf` → Character
  - `lafz` → String

## Language Syntax

### 1. Variables
```plaintext
adad x = 5;
asharia pi = 3.14;
lafz name = "Ali";
jhanda isTrue = sahi;
```

### 2. Arithmetic Operations
```plaintext
adad sum = 5 + 10;
asharia product = 2.5 * 4;
```

### 3. Conditional Statements
```plaintext
agar (x > 3) {
    likho("Bara hai!");
} warna {
    likho("Chota hai!");
}
```

### 4. Loops
```plaintext
jabtak (x < 10) {
    likho("X is: " + x);
    x = x + 1;
}
```

### 5. Input/Output
```plaintext
lafz name;
lo(name);
do("Hello, " + name);
```

### 6. Comments
```plaintext
~~ This is a single-line comment

-%
This is a
multi-line comment
%-
```

## Example Program
```plaintext
~~ Program to take input and print a message

lafz name;
lo(name);
do("Salam, " + name);

adad x = 10;
agar (x > 5) {
    likho("X is greater than 5 ");
} warna {
    likho("X is less than or equal to 5");
}
```

## How It Works
1. **Lexical Analysis**: The lexer processes the input code, tokenizing each element based on keywords, identifiers, operators, and delimiters.
2. **Parsing**: The parser interprets the tokens to validate syntax and execute statements accordingly.
3. **Execution**: The language supports basic control structures, mathematical operations, and user interaction through input/output functions.

## Future Enhancements
- Implementing conditional statements
- Support for functions and procedures
- Enhanced error handling and debugging tools
- More complex data structures like arrays and objects

## Conclusion
The Roman Urdu Programming Language simplifies programming using an intuitive, human-friendly syntax in Roman Urdu. It provides essential programming capabilities while maintaining ease of use for beginners and enthusiasts.

For contributions, feature requests, or improvements, feel free to modify and extend this project!


