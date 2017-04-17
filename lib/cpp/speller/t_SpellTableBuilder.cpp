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
            case  0: strcat(sequence, "r"); break; // ぁ
            case  1: strcat(sequence, "R"); break; // あ
            case  2: strcat(sequence, "s"); break; // い
            case  3: strcat(sequence, "e"); break; // ぇ 
            case  4: strcat(sequence, "E"); break; // え
            case  5: strcat(sequence, "f"); break; // ぉ
            case  6: strcat(sequence, "a"); break; // け
            case  7: strcat(sequence, "q"); break; // げ
            case  8: strcat(sequence, "Q"); break; // こ
            case  9: strcat(sequence, "t"); break; // さ
            case 10: strcat(sequence, "T"); break; // ざ
            case 11: strcat(sequence, "d"); break; // し
            case 12: strcat(sequence, "w"); break; // じ
            case 13: strcat(sequence, "W"); break; // す
            case 14: strcat(sequence, "c"); break; // ず
            case 15: strcat(sequence, "z"); break; // せ
            case 16: strcat(sequence, "x"); break; // ぜ
            case 17: strcat(sequence, "v"); break; // そ
            case 18: strcat(sequence, "g"); break; // ぞ
            default: strcat(sequence, " "); break;
            }

            switch( jung )
            {
            case  0: strcat(sequence, "k");  break; // た
            case  1: strcat(sequence, "o");  break; // だ
            case  2: strcat(sequence, "i");  break; // ち
            case  3: strcat(sequence, "O");  break; // ぢ
            case  4: strcat(sequence, "j");  break; // っ
            case  5: strcat(sequence, "p");  break; // つ
            case  6: strcat(sequence, "u");  break; // づ
            case  7: strcat(sequence, "P");  break; // て
            case  8: strcat(sequence, "h");  break; // で
            case  9: strcat(sequence, "hk"); break; // と
            case 10: strcat(sequence, "ho"); break; // ど
            case 11: strcat(sequence, "hl"); break; // な
            case 12: strcat(sequence, "y");  break; // に
            case 13: strcat(sequence, "n");  break; // ぬ
            case 14: strcat(sequence, "nj"); break; // ね
            case 15: strcat(sequence, "np"); break; // の
            case 16: strcat(sequence, "nl"); break; // は
            case 17: strcat(sequence, "b");  break; // ば
            case 18: strcat(sequence, "m");  break; // ぱ
            case 19: strcat(sequence, "ml"); break; // ひ
            case 20: strcat(sequence, "l");  break; // び
            default: strcat(sequence, " "); break;
            }

            switch( jong )
            {
            case  0: strcat(sequence, "");  break; // 
            case  1: strcat(sequence, "r");  break; // ぁ
            case  2: strcat(sequence, "R");  break; // あ
            case  3: strcat(sequence, "rt"); break; // ぃ
            case  4: strcat(sequence, "s");  break; // い
            case  5: strcat(sequence, "sw"); break; // ぅ
            case  6: strcat(sequence, "sg"); break; // う
            case  7: strcat(sequence, "e");  break; // ぇ
            case  8: strcat(sequence, "f");  break; // ぉ
            case  9: strcat(sequence, "fr"); break; // お
            case 10: strcat(sequence, "fa"); break; // か
            case 11: strcat(sequence, "fq"); break; // が
            case 12: strcat(sequence, "ft"); break; // き
            case 13: strcat(sequence, "fx"); break; // ぎ
            case 14: strcat(sequence, "fv"); break; // く
            case 15: strcat(sequence, "fg"); break; // ぐ
            case 16: strcat(sequence, "a");  break; // け
            case 17: strcat(sequence, "q");  break; // げ
            case 18: strcat(sequence, "qt"); break; // ご
            case 19: strcat(sequence, "t");  break; // さ
            case 20: strcat(sequence, "T");  break; // ざ
            case 21: strcat(sequence, "d");  break; // し
            case 22: strcat(sequence, "w");  break; // じ
            case 23: strcat(sequence, "c");  break; // ず
            case 24: strcat(sequence, "z");  break; // せ
            case 25: strcat(sequence, "x");  break; // ぜ
            case 26: strcat(sequence, "v");  break; // そ
            case 27: strcat(sequence, "g");  break; // ぞ
            default: strcat(sequence, " "); break;
            }

            strcat(sequence, "\"");

            fprintf(stderr, "    {0x%04X, {%7s, %d, SPELLTABLE_KOREAN}, 0},\n",
                    ch, sequence, strlen(sequence)-2);
        }
    }

    return 0;
}
