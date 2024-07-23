place_bombs__prologue:
        addiu   $sp, $sp, -12
        # convention -> save $s0 and $s1
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp) 
place_bombs__body:

        # TODO: convert this C function to MIPS

        # void place_bombs(int bad_row, int bad_col) {
        #   for (int i = 0; i < total_bombs; i++) {
        #     place_single_bomb(bad_row, bad_col);
        #   }
        # }
        
        move    $s0, $a0 
        move    $s1, $a1
        li      $t0, 0
place_bombs_loop1:
        bge     $t0, total_bombs, place_bombs__epilogue
        addi    $sp, $sp, -4         # move the stack pointer down to make room 
        sw      $ra, 0($sp) 
        # convention: all params we want to pass through, are loaded into $a registers
        move    $a0, $s0 
        move    $a1, $s1
        jal     place_single_bomb

        lw      $ra, 0($sp)
        addi    $sp, $sp, 4
        addi    $t0, $t0, 1             # i++;
        b       place_bombs_loop1

place_bombs__epilogue:
        lw      $ra, 0($sp)
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        addiu   $sp, $sp, 12

        jr      $ra