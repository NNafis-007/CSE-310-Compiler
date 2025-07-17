.MODEL SMALL
.STACK 1000H
.DATA
	number DB "00000$"
	x DW 1 DUP (0000h)
	y DW 1 DUP (0000h)
.CODE
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP

	MOV AX, 5		; Line 5
	MOV x, AX		; Line 5

	MOV AX, x
	MOV y, AX		; Line 6

	MOV AX, y
	MOV x, AX		; Line 7

	MOV AX, x		; Line 8
	CALL print_output
	CALL new_line

	MOV AX, 0		; Line 9
	JMP L1
L1:
	ADD SP, 0
	POP BP
	MOV AX, 4Ch
	INT 21h
main ENDP
new_line proc
    push ax
    push dx
    mov ah,2
    mov dl,0Dh
    int 21h
    mov ah,2
    mov dl,0Ah
    int 21h
    pop dx
    pop ax
    ret
    new_line endp
print_output proc  ;print what is in ax
    push ax
    push bx
    push cx
    push dx
    push si
    lea si,number
    mov bx,10
    add si,4
    cmp ax,0
    jnge negate
    print:
    xor dx,dx
    div bx
    mov [si],dl
    add [si],'0'
    dec si
    cmp ax,0
    jne print
    inc si
    lea dx,si
    mov ah,9
    int 21h
    pop si
    pop dx
    pop cx
    pop bx
    pop ax
    ret
    negate:
    push ax
    mov ah,2
    mov dl,'-'
    int 21h
    pop ax
    neg ax
    jmp print
    print_output endp
END main
