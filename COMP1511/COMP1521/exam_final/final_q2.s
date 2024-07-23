# read two integers and print all the integers which have their bottom 2 bits set.

main:
    li      $v0, 5
    syscall
    move    $t0, $v0                   # x = $t0

    li      $v0, 5
    syscall
    move    $t1, $v0                   # y = $t1

    li      $t2, 0x3                   # bit_mask = 0x3 = $t2

    addi    $t3, $t0, 1                # int c = x + 1;

loop:
    bge     $t3, $t1, end

    and     $t4, $t3, $t2              # c & bit_mask

    bne     $t4, $t2, skip

    move    $a0, $t3
    li      $v0, 1
    syscall
    li      $a0, '\n'
    li      $v0, 11
    syscall

skip:
    addi    $t3, $t3, 1                # c++;

    b       loop
end:
    li $v0, 0
    jr $31
