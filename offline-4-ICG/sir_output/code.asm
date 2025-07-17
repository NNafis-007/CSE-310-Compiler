.MODEL SMALL
.STACK 1000H
.Data
	number DB "00000$"
	w DW 10 DUP (0000H)
.CODE
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP
	SUB SP, 2
L1:
	SUB SP, 20
L2:
	MOV AX, 1       ; Line 6
	PUSH AX
	MOV AX, 10       ; Line 6
	POP BX
	PUSH AX
	MOV AX, 2
	MUL BX
	MOV BX, AX
	MOV AX, 22
	SUB AX, BX
	MOV BX, AX
	POP AX
	MOV SI, BX
	NEG SI
	MOV [BP+SI], AX
	PUSH AX
	POP AX
L3:
	MOV AX, 1       ; Line 7
	PUSH AX
	POP BX
	MOV AX, 2       ; Line 7
	MUL BX
	MOV BX, AX
	MOV AX, 22
	SUB AX, BX
	MOV BX, AX
	MOV SI, BX
	NEG SI
	MOV AX, [BP+SI]
	MOV [BP-2], AX
	PUSH AX
	POP AX
L4:
	MOV AX, [BP-2]       ; Line 14
	CALL print_output
	CALL new_line
L5:
	MOV AX, 0       ; Line 40
	JMP L7
L6:
L7:
	ADD SP, 22
	POP BP
	MOV AX,4CH
	INT 21H
main ENDP
;-------------------------------
;         print library         
;-------------------------------
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
;-------------------------------
END main