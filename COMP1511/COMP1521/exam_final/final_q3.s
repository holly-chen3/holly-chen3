# read an integer
# print 1 iff  the least significant bit is equal to the most significant bit
# print 0 otherwise

main:
    li      $v0, 5
    syscall
    move    $t0, $v0

    andi    $t1, $t0, 0x1

    sra     $t2, $t0, 31    

    andi    $t3, $t2, 0x1

    bne     $t1, $t3, skip

    li $a0, 1
    li $v0, 1
    syscall

    li   $a0, '\n'
    li   $v0, 11
    syscall

    b   end

skip:
    li $a0, 0
    li $v0, 1
    syscall

    li   $a0, '\n'
    li   $v0, 11
    syscall
    # print out 0

end:
    li $v0, 0
    jr $31
