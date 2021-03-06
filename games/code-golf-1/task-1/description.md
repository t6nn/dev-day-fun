# Code Golf - Reversing an integer

## Input
Provided from standard input via `System.in`. Positive long (64-bit) integers between 0 and 9999999999999999 (16 positions), 
on one single line, separated by spaces.
Example:

    345921 2000 0 1234

## Task
Calculate the decimal reverse of an integer - a new integer where the numbers of the integer 
are reversed, omitting leading zeros. For example:
- 345921 -> 129543
- 2000 -> 2 (instead of 0002)
- 0 -> 0

## Output
The reversed integers, separated by spaces, sent to standard output via `System.out`. No other characters at the end of the line (no trailing space or newline).
Example (for the input above):

    129543 2 0 4321

## Code Golf
Solutions compete against each other, based on the number of bytes in your source file. The solution
*must* be correct. The player with the least total number of bytes over all assignments wins.
