# read 10 numbers into an array

main:

    li   $t0, 0          # i = 0
loop0:
    bge  $t0, 10, loop0_end   # while (i < 10) {

    li   $v0, 5          #   scanf("%d", &numbers[i]);
    syscall              #

    mul  $t1, $t0, 4     #   calculate &numbers[i]
    la   $t2, numbers    #
    add  $t3, $t1, $t2   #
    sw   $v0, ($t3)      #   store entered number in array

    add  $t0, $t0, 1     #   i++;
    b    loop0           # }

loop0_end:
    li   $t0, 0

loop1:
    bge  $t0, 10, loop1_end   # while (i < 10) {
    
    mul  $t1, $t0, 4
    la   $t2, numbers
    add  $t3, $t1, $t2
    lw   $t4, ($t3)

    ble  $t4, 0, loop1_skip

    move $a0, $t4
    li   $v0, 1
    syscall

    li   $a0, '\n'
    li   $v0, 11
    syscall

loop1_skip:
    addi $t0, $t0, 1

    b    loop1
loop1_end:
    li   $a0, '\n'
    li   $v0, 11
    syscall

    li   $t0, 0

loop2:
    bge  $t0, 10, end    # while (i < 10) {
    
    mul  $t1, $t0, 4
    la   $t2, numbers
    add  $t3, $t1, $t2
    lw   $t4, ($t3)

    bge  $t4, 0, loop2_skip

    move $a0, $t4
    li   $v0, 1
    syscall

    li   $a0, ' '
    li   $v0, 11
    syscall

loop2_skip:
    addi $t0, $t0, 1

    b    loop2

end:
    li   $a0, '\n'
    li   $v0, 11
    syscall
    
    li   $v0, 0
    jr   $31             # return

.data

numbers:
    .word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0  # int numbers[10] = {0};
