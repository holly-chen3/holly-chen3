# this code reads a line of input and prints 42
# change it to check the string for brackets

# read a line of input and checks whether it consists only of balanced brackets
# if the line contains characters other than ()[]{} -1 is printed
# if brackets are not balance,  -1 is printed
# if the line contains only balanced brackets, length of the line is printed
main__prologue:
    addiu   $sp, $sp, -4
    sw      $ra, 0($sp)
main:
    la      $a0, line
    la      $a1, 1024
    li      $v0, 8          # fgets(line, 1024, stdin);
    syscall

    li      $a0, 0
    li      $a1, '\n'

    jal     check

    move    $t0, $v0        # int r = check(0, '\n');
    
    move    $a0, $t0
    li      $v0, 1
    syscall

    li      $a0, '\n'
    li      $v0, 11
    syscall
    
    li      $v0, 0          # return 0
    jr      $31
main__epilogue:
    lw      $ra, 0($sp)
    addiu   $sp, $sp, 4
check:

check__prologue:
    addiu   $sp, $sp, -12
    sw      $ra, 0($sp)
    sw      $s0, 4($sp)
    sw      $s1, 8($sp)
check__body:
    move    $s0, $a0
    move    $s1, $a1

    mul     $t1, $s0, 4
    la      $t2, line
    add     $t3, $t1, $t2
    lw      $t4, ($t3)

    bne     $t4, $s1, check__else
    addi    $s0, $s0, 1     # index = index + 1;

    b       check__epilogue
check__else:
    move    $a0, $s0

    jal     match 

    move    $s0, $v0

    ble     $s0, 0, check__epilogue

    move    $a0, $s0       # index = check(index, what);
    move    $a1, $s1

    jal     check

    move    $s0, $v0
    
check__epilogue:
    move    $v0, $s0

    lw      $ra, 0($sp)
    lw      $s0, 4($sp)
    lw      $s1, 8($sp)
    addiu   $sp, $sp, 12

    jr      $ra

match:

match__prologue:
    addiu   $sp, $sp, -8
    sw      $ra, 0($sp)
    sw      $s0, 4($sp)
match__body:
    move    $s0, $a0

    li      $t0, -1         # int r = -1;

    mul     $t1, $s0, 4
    la      $t2, line
    add     $t3, $t1, $t2
    lw      $t4, ($t3)      # int c = line[index];

    li      $t1, 0          # int w = 0;

    beq     $t4, '[', match__then1
    beq     $t4, '(', match__then2
    beq     $t4, '{', match__then3

    b       match__end
match__then1:
    li      $t1, ']'
    b       match__end
match__then2:
    li      $t1, ')' 
    b       match__end
match__then3:
    li      $t1, '}'
    b       match__end
match__end:
    beq     $t1, 0, match__epilogue

    addi    $a0, $s0, 1      # r = check(index + 1, w);
    move    $a1, $t1

    jal     check

    move    $t0, $v0

match__epilogue:
    move    $v0, $t0

    lw      $ra, 0($sp)
    lw      $s0, 4($sp)
    addiu   $sp, $sp, 8

    jr      $ra

# PUT YOU FUNCTION DEFINITIONS HERE


.data
line:
    .byte 0:1024
