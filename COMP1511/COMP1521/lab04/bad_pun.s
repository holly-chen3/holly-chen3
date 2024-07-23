# A simple C program that attempts to be punny
# Written by Holly Chen (z5359932)
# On 05/10/2021

main:
    # ... pass address of string as argument
    la $a0, string
    # ... 4 is printf "%s" syscall number
    li $v0, 4
    syscall
    li $v0, 0
    jr $ra

    .data
string:
    .asciiz "I MIPS you!\n"