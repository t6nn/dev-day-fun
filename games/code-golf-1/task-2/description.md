# Code golf - Finding connected blocks

## Input
A string representing a matrix (a two dimensional array) of 0s and 1s. Elements are separated by commas while rows of the matrix are separated by symbol '$'.

## Task
Given a matrix, find the number of 1s in the biggest connected block of 1s (connection can be made left, right, up, down or diagonally).
Example:

    1,1,1,0,0,0,1,1$
    1,0,0,0,0,0,1,1$
    0,0,1,1,0,0,1,1$
    0,1,0,0,1,0,0,0$
    0,1,0,0,1,0,0,0$
    0,1,0,0,1,0,0,0$
    0,0,1,1,0,0,0,0$
    0,0,0,0,0,0,0,0$

In this example there are three blocks we can consider, one in upper left corner of size 4, one in upper right corner of size 6
 and one more more towards the center looking like a circle of size 10.
 
## Output
Number representing the count of 1s in the biggest connected block.

## Code Golf
Solutions compete against each other, based on the number of bytes in your source file. The solution
*must* be correct, the solution with the smallest source file wins.