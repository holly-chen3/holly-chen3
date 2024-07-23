# read a line and print whether it is a palindrom

main:
    la   $a0, str0       # printf("Enter a line of input: ");
    li   $v0, 4
    syscall

    la   $a0, line
    la   $a1, 256
    li   $v0, 8          # fgets(buffer, 256, stdin)
    syscall              #

    li   $t0, 0
loop:
    la   $t2, line
    add  $t1, $t0, $t2   # &line[i]
    lb   $t2, ($t1)
    beqz $t2, loop_end

    addi $t0, $t0, 1

    b    loop
loop_end:
    li   $t1, 0
    addi $t2, $t0, -2
loop2:
    bge  $t1, $t2, loop2_end

    la   $t3, line
    add  $t4, $t1, $t3
    lb   $t5, ($t4)

    add  $t6, $t2, $t3
    lb   $t7, ($t6)

    beq  $t5, $t7, skip
    
    la   $a0, not_palindrome
    li   $v0, 4
    syscall

    b    end
skip:
    addi $t1, $t1, 1
    addi $t2, $t2, -1

    b    loop2
loop2_end:
    la   $a0, palindrome
    li   $v0, 4
    syscall

end:
    li   $v0, 0          # return 0
    jr   $ra


.data
str0:
    .asciiz "Enter a line of input: "
palindrome:
    .asciiz "palindrome\n"
not_palindrome:
    .asciiz "not palindrome\n"


# line of input stored here
line:
    .space 256

