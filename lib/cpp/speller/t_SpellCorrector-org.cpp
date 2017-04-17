// speller
#include "SpellCorrector.h"
#include <iostream>
#include <string>
#include <fstream>
using namespace speller;

int main(void)
{
    const bool ONLY_OTA_EXIST = true;
    const bool EVERY_CORRECT_WORD = false;
    SpellDictionary dic(EVERY_CORRECT_WORD,"n");
    dic.load("./");

    SpellCorrector corrector(&dic, 2);

    char input[1024];
    while( true )
    {
        printf("input> "); fflush(stdout);
        if( !fgets(input, 1024, stdin) ) break;
        input[strlen(input)-1] = 0;
        {
            printf("result for word\n");

            char** result;
            int    size;
            size = corrector.suggestWord(input, result, CHARSET::UNICODE_CP949, 50);


            // print and free
            int i;
            for( i = 0; i < size; i++ )
            {
                printf("#%02d: [%s]\n", i, result[i]);
                DELETE_ARRAY(result[i]);
            }
            DELETE_ARRAY(result);
        }
        {
            printf("result for sentence\n");

            char* result;
            int   exist;
            exist = corrector.suggestSentence(input, result, CHARSET::UNICODE_CP949);

            // print and free
            if( exist )
            {
                printf("# [%s]\n", result);
                DELETE_ARRAY(result);
            }
        }
    }

    return 0;
}
