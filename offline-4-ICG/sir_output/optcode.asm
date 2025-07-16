.MODEL SMALL
.STACK 1000H
.Data
	number DB "00000$"
.CODE
f PROC
	PUSH BP
	MOV BP, SP
	SUB SP, 2
L1:
	MOV AX, 5       ; Line 3
	MOV [BP-2], AX
L2:
L3:
	MOV AX, 0       ; Line 4
	MOV DX, AX
	MOV AX, [BP-2]       ; Line 4
	CMP AX, DX
	JG L4
	JMP L7
L4:
	MOV AX, [BP+4]       ; Line 5
	PUSH AX
	INC AX
	MOV [BP+4], AX
	POP AX
L5:
	MOV AX, [BP-2]       ; Line 6
	PUSH AX
	DEC AX
	MOV [BP-2], AX
	POP AX
L6:
	JMP L3
L7:
	MOV AX, [BP+4]       ; Line 8
	MOV CX, AX
	MOV AX, 3       ; Line 8
	CWD
	MUL CX
	PUSH AX
	MOV AX, 7       ; Line 8
	MOV DX, AX
	POP AX       ; Line 8
	SUB AX, DX
	PUSH AX
	POP AX       ; Line 8
	JMP L10
L8:
	MOV AX, 9       ; Line 9
	MOV [BP+4], AX
L9:
L10:
	ADD SP, 2
	POP BP
	RET 2
f ENDP
g PROC
	PUSH BP
	MOV BP, SP
	SUB SP, 2
	SUB SP, 2
L11:
	MOV AX, [BP+6]       ; Line 15
	PUSH AX
	CALL f
	PUSH AX
	MOV AX, [BP+6]       ; Line 15
	MOV DX, AX
	POP AX       ; Line 15
	ADD AX, DX
	PUSH AX
	MOV AX, [BP+4]       ; Line 15
	MOV DX, AX
	POP AX       ; Line 15
	ADD AX, DX
	PUSH AX
	POP AX       ; Line 15
	MOV [BP-2], AX
L12:
	MOV AX, [BP-2]       ; Line 16
	CALL print_output
	CALL new_line
L13:
	MOV AX, 0       ; Line 18
	MOV [BP-4], AX
L14:
	MOV AX, 7       ; Line 18
	MOV DX, AX
	MOV AX, [BP-4]       ; Line 18
	CMP AX, DX
	JL L16
	JMP L22
L15:
	MOV AX, [BP-4]       ; Line 18
	PUSH AX
	INC AX
	MOV [BP-4], AX
	POP AX
	JMP L14
L16:
	MOV AX, 3       ; Line 19
	MOV CX, AX
	MOV AX, [BP-4]       ; Line 19
	CWD
	DIV CX
	PUSH DX
	MOV AX, 0       ; Line 19
	MOV DX, AX
	POP AX       ; Line 19
	CMP AX, DX
	JE L17
	JMP L19
L17:
	MOV AX, 5       ; Line 20
	MOV DX, AX
	MOV AX, [BP-2]       ; Line 20
	ADD AX, DX
	PUSH AX
	POP AX       ; Line 20
	MOV [BP-2], AX
L18:
	JMP L15
L19:
	MOV AX, 1       ; Line 23
	MOV DX, AX
	MOV AX, [BP-2]       ; Line 23
	SUB AX, DX
	PUSH AX
	POP AX       ; Line 23
	MOV [BP-2], AX
L20:
L21:
	JMP L15
L22:
	MOV AX, [BP-2]       ; Line 27
	JMP L24
L23:
L24:
	ADD SP, 4
	POP BP
	RET 4
g ENDP
main PROC
	MOV AX, @DATA
	MOV DS, AX
	PUSH BP
	MOV BP, SP
	SUB SP, 2
	SUB SP, 2
	SUB SP, 2
L25:
	MOV AX, 1       ; Line 32
	MOV [BP-2], AX
L26:
	MOV AX, 2       ; Line 33
	MOV [BP-4], AX
L27:
	MOV AX, [BP-2]       ; Line 34
	PUSH AX
	MOV AX, [BP-4]       ; Line 34
	PUSH AX
	CALL g
	PUSH AX
	POP AX       ; Line 34
	MOV [BP-2], AX
L28:
	MOV AX, [BP-2]       ; Line 35
	CALL print_output
	CALL new_line
L29:
	MOV AX, 0       ; Line 46
	JMP L31
L30:
L31:
	ADD SP, 6
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
