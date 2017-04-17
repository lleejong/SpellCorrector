#include "SpellDictionary.h"

namespace speller {

SpellDictionary::SpellDictionary(bool bOnlyUserWord, const char* pOtaClass)
{
    mpWordList = 0;
    mnWordSize = 0;
    mnWordMax  = 0;
    mbOnlyUserWord = bOnlyUserWord;
    mpOtaClass = pOtaClass;
}

SpellDictionary::~SpellDictionary()
{
}

int SpellDictionary::load(const char* pPath)
{
    // make dictionary filename
    CharFixedString<MAX_PATH_LEN+1> filename;
    filename = pPath;
    filename += PATH_DELIMITER;
    filename += SPELL_DICTIONARY_FILE;

    // check exist file
    FileStat fstat;
    if( File::getStat(filename.c_str(), &fstat) == 0 )
    {
        // read dictionary
        char* buf = new char[fstat.mSize+1];
        if( buf )
        {
            File file;
            if( file.open(filename.c_str(), File::OPEN_RDONLY) == 0 )
            {
                if( file.read(buf, fstat.mSize) != fstat.mSize )
                    return -1;

                buf[fstat.mSize] = 0; // set null

                file.close();
            }
        }

        if( buf )
        {
            char* line;
            char* linesave;
            char* word;
            char* weight;
            char* userword;
            char* tokensave;

            int lineno = 0;
            unsigned int userweight = 0;

            linesave = buf;
            while( line = strtok_r(linesave, "\r\n", &linesave) )
            {
                lineno++;

                word   = strtok_r(line, " \t", &tokensave);
                weight = strtok_r(tokensave, " \t", &tokensave);

                if( word && weight )
                {
                    userweight = atoi(weight);
                    if( userweight > 0xFFFF )
                        userweight = 0xFFFF;

                    if( userweight == 0 && strcmp(weight, "0") )
                    {
                        fprintf(stderr, "error: spell dictionary weight (word:%s, line:%d)\n", word, lineno);
                        continue;
                    }

                    if( mbOnlyUserWord == false )
                    {
                        if( add(word, word, userweight) < 0 )
                        {
                            DELETE_ARRAY( buf );
                            return -1;
                        }
                    }

                    while( userword = strtok_r(tokensave, " \t", &tokensave) )
                    {
                        userweight |= USER_WORD_MASK;

                        if( add(word, userword, userweight) < 0 )
                        {
                            DELETE_ARRAY( buf );
                            return -1;
                        }
                    }
                }
                else
                {
                    if( word )
                    {
                        fprintf(stderr, "error: spell dictionary format (word:%s, line:%d)\n", word, lineno);
                    }
                }
            }

            DELETE_ARRAY( buf );
        }
    }
    else
        return 1;

    return 0;
}

int SpellDictionary::add(const char* pCorrect, const char* pCandidate, unsigned int nWeight)
{
    if( mnWordSize >= mnWordMax )
    {
        // compute expaned size
        int nNeedSize = (mnWordMax + 1024);

        // allocate expaned word list
        Word** pWordList = new Word*[nNeedSize];
        if( pWordList == 0 )
            return -1;

        // copy
        memcpy(pWordList, mpWordList, sizeof(Word*)*mnWordMax);

        // free old word list
        DELETE_ARRAY( mpWordList );

        // change
        mpWordList = pWordList;
        mnWordMax  = nNeedSize;
    }

    // allocate word
    mpWordList[mnWordSize] = new Word();
    if( mpWordList[mnWordSize] == 0 )
    {
        return -1;
    }

    // convert utf-8 to unicode
    mpWordList[mnWordSize]->word = Unicode::convertMBStoWCS(pCorrect, UNICODE_UTF8);
    if( mpWordList[mnWordSize]->word == 0 )
    {
        DELETE_SINGLE( mpWordList[mnWordSize] );
        return -1;
    }

    // get keyboard sequence
    WChar* pWord = 0x00;
    if( pCorrect == pCandidate )
    {
        pWord = mpWordList[mnWordSize]->word;
    }
    else
    {
        pWord = Unicode::convertMBStoWCS(pCandidate, UNICODE_UTF8);
    }

    mpWordList[mnWordSize]->sequence =
        SpellTable::getInstance()->convert2KeyboardSequence(pWord);

    if( pWord != mpWordList[mnWordSize]->word ) DELETE_ARRAY( pWord );
    if( mpWordList[mnWordSize]->sequence == 0x00 )
    {
        DELETE_SINGLE( mpWordList[mnWordSize] );
        DELETE_ARRAY( mpWordList[mnWordSize]->word );
        return -1;
    }

    // get language
    mpWordList[mnWordSize]->language =
        SpellTable::getInstance()->getLanguage(mpWordList[mnWordSize]->word);

    // set weight
    mpWordList[mnWordSize]->weight = nWeight;

    mnWordSize++;

    return 0;
}

} // namespace speller
