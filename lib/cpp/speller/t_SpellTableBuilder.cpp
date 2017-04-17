#include <stdio.h>
#include <string.h>

int main(void)
{
    unsigned int ch;

    for( ch = 1; ch <= 0xFFFF; ch++ )
    {
        if( (ch >= '!') && (ch <= '~') )
        {
            if( (ch == '"') || (ch == '\\') )
                fprintf(stderr, "    {0x%04X, {\"\\%c\", 1, SPELLTABLE_ENGLISH}, 0},\n",
                        ch, ch);
            else
                fprintf(stderr, "    {0x%04X, { \"%c\", 1, SPELLTABLE_ENGLISH}, 0},\n",
                        ch, ch);
        }
        if( (ch >= 0xAC00) && (ch <= 0xD7A3) )
        {
            unsigned int wch = ch - 0xAC00;
            unsigned int cho  = wch / 588;
            unsigned int jung = (wch % 588) / 28;
            unsigned int jong = (wch % 588) % 28;
            char sequence[1024] = {0, };

            strcat(sequence, "\"");

            switch( cho )
            {
            case  0: strcat(sequence, "r"); break; // ��
            case  1: strcat(sequence, "R"); break; // ��
            case  2: strcat(sequence, "s"); break; // ��
            case  3: strcat(sequence, "e"); break; // �� 
            case  4: strcat(sequence, "E"); break; // ��
            case  5: strcat(sequence, "f"); break; // ��
            case  6: strcat(sequence, "a"); break; // ��
            case  7: strcat(sequence, "q"); break; // ��
            case  8: strcat(sequence, "Q"); break; // ��
            case  9: strcat(sequence, "t"); break; // ��
            case 10: strcat(sequence, "T"); break; // ��
            case 11: strcat(sequence, "d"); break; // ��
            case 12: strcat(sequence, "w"); break; // ��
            case 13: strcat(sequence, "W"); break; // ��
            case 14: strcat(sequence, "c"); break; // ��
            case 15: strcat(sequence, "z"); break; // ��
            case 16: strcat(sequence, "x"); break; // ��
            case 17: strcat(sequence, "v"); break; // ��
            case 18: strcat(sequence, "g"); break; // ��
            default: strcat(sequence, " "); break;
            }

            switch( jung )
            {
            case  0: strcat(sequence, "k");  break; // ��
            case  1: strcat(sequence, "o");  break; // ��
            case  2: strcat(sequence, "i");  break; // ��
            case  3: strcat(sequence, "O");  break; // ��
            case  4: strcat(sequence, "j");  break; // ��
            case  5: strcat(sequence, "p");  break; // ��
            case  6: strcat(sequence, "u");  break; // ��
            case  7: strcat(sequence, "P");  break; // ��
            case  8: strcat(sequence, "h");  break; // ��
            case  9: strcat(sequence, "hk"); break; // ��
            case 10: strcat(sequence, "ho"); break; // ��
            case 11: strcat(sequence, "hl"); break; // ��
            case 12: strcat(sequence, "y");  break; // ��
            case 13: strcat(sequence, "n");  break; // ��
            case 14: strcat(sequence, "nj"); break; // ��
            case 15: strcat(sequence, "np"); break; // ��
            case 16: strcat(sequence, "nl"); break; // ��
            case 17: strcat(sequence, "b");  break; // ��
            case 18: strcat(sequence, "m");  break; // ��
            case 19: strcat(sequence, "ml"); break; // ��
            case 20: strcat(sequence, "l");  break; // ��
            default: strcat(sequence, " "); break;
            }

            switch( jong )
            {
            case  0: strcat(sequence, "");  break; // 
            case  1: strcat(sequence, "r");  break; // ��
            case  2: strcat(sequence, "R");  break; // ��
            case  3: strcat(sequence, "rt"); break; // ��
            case  4: strcat(sequence, "s");  break; // ��
            case  5: strcat(sequence, "sw"); break; // ��
            case  6: strcat(sequence, "sg"); break; // ��
            case  7: strcat(sequence, "e");  break; // ��
            case  8: strcat(sequence, "f");  break; // ��
            case  9: strcat(sequence, "fr"); break; // ��
            case 10: strcat(sequence, "fa"); break; // ��
            case 11: strcat(sequence, "fq"); break; // ��
            case 12: strcat(sequence, "ft"); break; // ��
            case 13: strcat(sequence, "fx"); break; // ��
            case 14: strcat(sequence, "fv"); break; // ��
            case 15: strcat(sequence, "fg"); break; // ��
            case 16: strcat(sequence, "a");  break; // ��
            case 17: strcat(sequence, "q");  break; // ��
            case 18: strcat(sequence, "qt"); break; // ��
            case 19: strcat(sequence, "t");  break; // ��
            case 20: strcat(sequence, "T");  break; // ��
            case 21: strcat(sequence, "d");  break; // ��
            case 22: strcat(sequence, "w");  break; // ��
            case 23: strcat(sequence, "c");  break; // ��
            case 24: strcat(sequence, "z");  break; // ��
            case 25: strcat(sequence, "x");  break; // ��
            case 26: strcat(sequence, "v");  break; // ��
            case 27: strcat(sequence, "g");  break; // ��
            default: strcat(sequence, " "); break;
            }

            strcat(sequence, "\"");

            fprintf(stderr, "    {0x%04X, {%7s, %d, SPELLTABLE_KOREAN}, 0},\n",
                    ch, sequence, strlen(sequence)-2);
        }
    }

    return 0;
}
