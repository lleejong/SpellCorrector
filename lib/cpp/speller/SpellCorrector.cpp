#include "SpellCorrector.h"
#include <iostream>
namespace speller {

bool result_compare(SpellCorrector::Result a, SpellCorrector::Result b)
{
    return a.weight > b.weight;
}

SpellCorrector::SpellCorrector(SpellDictionary* pSpellDictionary, int nMinDistance)
{
    // set dictionary
    mpSpellDictionary = pSpellDictionary;
    mpWord            = mpSpellDictionary->word();
    mnDictionarySize  = mpSpellDictionary->size();

    // set distance
    mnMinDistance = nMinDistance;

    // set node
    mnMaxNode = (unsigned int)pow(3.0, (double)mnMinDistance);
    mpNode1   = 0;
    mpNode2   = 0;

    // set ota class
    mpOta = OtaInterface::create(mpSpellDictionary->mpOtaClass);
}

SpellCorrector::~SpellCorrector()
{
    DELETE_ARRAY( mpNode1 );
    DELETE_ARRAY( mpNode2 );
    DELETE_SINGLE( mpOta );
}

int SpellCorrector::suggestWord(const char* pIncorrectWord, char**& pSuggestWord, int nEncoding, int nSuggestCount)
{
    // convert input to unicode
    WChar* pIncorrectWordW = Unicode::convertMBStoWCS(pIncorrectWord, nEncoding);
    if( pIncorrectWordW )
    {
        int i;

        // get suggest list
        vector<Result> result;
        int size = suggest(pIncorrectWordW, &result);

        // allocate result
        pSuggestWord = new char*[size];
        if( pSuggestWord == 0 )
        {
            DELETE_ARRAY( pIncorrectWordW );
            return 0;
        }

        // assign
        for( i = 0; i < size && i < nSuggestCount; i++ )
        {
            pSuggestWord[i] = Unicode::convertWCStoMBS(mpWord[ result[i].word ]->word, nEncoding);
            if( pSuggestWord[i] == 0 )
                break;
        }

        // reset size
        size = i;

        // free
        DELETE_ARRAY( pIncorrectWordW );
        return size;
    }

    return 0;
}

int SpellCorrector::suggestSentence(const char* pIncorrectSentence, char*& pSuggestSentence, int nEncoding)
{
    // convert input to unicode
    WChar* pIncorrectSentenceW = Unicode::convertMBStoWCS(pIncorrectSentence, nEncoding);
    if( pIncorrectSentenceW )
    {
        WChar* pSuggestSentenceW = 0x00;
        int    nAllocatedSentenceW = 0;
        int    nPosSentenceW = 0;
        int    nCopyLength = 0;
        bool   bIsSuggested = false;

        WChar wChar = 0;

        // find
        WChar* pBeginW = pIncorrectSentenceW;
        WChar* pEndW = 0x00;
        while( *pBeginW )
        {
            // skip non-characters
            pEndW = pBeginW;
            while( *pEndW && ucs2_issymbol(*pEndW) )
                pEndW++;

            // copy non-characters
            if( (pEndW - pBeginW) > 0 )
            {
                nCopyLength = (int)(pEndW - pBeginW);

                // check growth
                if( nAllocatedSentenceW <= (nPosSentenceW + nCopyLength) )
                {
                    int nNewAllocatgedSentenceW = 0;
                    if( nCopyLength >= 1024 )
                    {
                        nNewAllocatgedSentenceW = nAllocatedSentenceW + nCopyLength;
                    }
                    else
                    {
                        nNewAllocatgedSentenceW = nAllocatedSentenceW + 1024;
                    }

                    // new allocation
                    WChar* pTempW = new WChar[nNewAllocatgedSentenceW+1];
                    if( pTempW == 0x00 )
                        break;

                    if( pSuggestSentenceW )
                    {
                        memcpy( pTempW, pSuggestSentenceW, sizeof(WChar) * nAllocatedSentenceW );
                        DELETE_ARRAY( pSuggestSentenceW );
                    }

                    // swap
                    pSuggestSentenceW = pTempW;
                    nAllocatedSentenceW = nNewAllocatgedSentenceW;
                }

                memcpy( &pSuggestSentenceW[nPosSentenceW], pBeginW, sizeof(WChar) * nCopyLength );
                nPosSentenceW += nCopyLength;
                pSuggestSentenceW[nPosSentenceW] = 0; // set null
            }

            // check null
            if( *pEndW == 0 )
                break;

            // move pointer
            pBeginW = pEndW;

            // find characters
            pEndW = pBeginW;
            while( *pEndW && !ucs2_issymbol(*pEndW) )
                pEndW++;

            // suggest
            if( (pEndW - pBeginW) > 0 )
            {
                // mark null temporary
                wChar = *pEndW;
                *pEndW = 0;

                // get suggest list
                vector<Result> result;
                int size = suggest(pBeginW, &result);

                // copy suggest word
                WChar* pCopyString = 0x00;
                if( size > 0 )
                {
                    pCopyString = mpWord[ result[0].word ]->word;
                    nCopyLength = ucs2_strlen(pCopyString);

                    bIsSuggested = true;
                }
                else
                {
                    if( mpOta )
                    {
                        pCopyString = (spr::WChar*)mpOta->correct(pBeginW);
                        if( pCopyString != 0x00 )
                            bIsSuggested = true;
                    }
                    else
                    {
                        pCopyString = 0x00;
                    }

                    if( pCopyString == 0x00 )
                    {
                        pCopyString = pBeginW;
                    }
                    nCopyLength = ucs2_strlen(pCopyString);
                }

                // check growth
                if( nAllocatedSentenceW <= (nPosSentenceW + nCopyLength) )
                {
                    int nNewAllocatgedSentenceW = 0;
                    if( nCopyLength >= 1024 )
                    {
                        nNewAllocatgedSentenceW = nAllocatedSentenceW + nCopyLength;
                    }
                    else
                    {
                        nNewAllocatgedSentenceW = nAllocatedSentenceW + 1024;
                    }

                    // new allocation
                    WChar* pTempW = new WChar[nNewAllocatgedSentenceW+1];
                    if( pTempW == 0x00 )
                        break;

                    if( pSuggestSentenceW )
                    {
                        memcpy( pTempW, pSuggestSentenceW, sizeof(WChar) * nAllocatedSentenceW );
                        DELETE_ARRAY( pSuggestSentenceW );
                    }

                    // swap
                    pSuggestSentenceW = pTempW;
                    nAllocatedSentenceW = nNewAllocatgedSentenceW;
                }

                memcpy( &pSuggestSentenceW[nPosSentenceW], pCopyString, sizeof(WChar) * nCopyLength );
                nPosSentenceW += nCopyLength;
                pSuggestSentenceW[nPosSentenceW] = 0; // set null

                // restore
                *pEndW = wChar;
            }

            // move pointer
            pBeginW = pEndW;
        }

        // convert unicode to multi-byte
        if( pSuggestSentenceW && (bIsSuggested == true) )
        {
            pSuggestSentence = Unicode::convertWCStoMBS(pSuggestSentenceW, nEncoding);
            // free
            DELETE_ARRAY( pSuggestSentenceW );
        }
        else
        {
            pSuggestSentence = 0;
        }

        // free
        DELETE_ARRAY( pSuggestSentenceW );
        DELETE_ARRAY( pIncorrectSentenceW );

        // return exist or not
        if( pSuggestSentence )
            return 1;
    }

    return 0;
}

int SpellCorrector::suggest(const WChar* pIncorrectWord, vector<Result>* pResult)
{
    unsigned int i;

    // allocate nodes
    mpNode1 = new Node[mnMaxNode];
    mpNode2 = new Node[mnMaxNode];
    if( (mpNode1 == 0) || (mpNode2 == 0) )
    {
        DELETE_ARRAY( mpNode1 );
        DELETE_ARRAY( mpNode2 );
        return 0;
    }

    // get keyboard sequence
    char* pSequence =
        SpellTable::getInstance()->convert2KeyboardSequence(pIncorrectWord);
    if( pSequence == 0 )
    {
        DELETE_ARRAY( mpNode1 );
        DELETE_ARRAY( mpNode2 );
        return 0;
    }

    unsigned int nLanguage = SpellTable::getInstance()->getLanguage(pIncorrectWord);

    // get candidate words
    int nSequenceLength = strlen(pSequence);
    for( i = 0; i < mnDictionarySize; i++ )
    {
        int nDistance = edits(pSequence, i);
        if( nDistance >= 0 && (nSequenceLength > nDistance) )
        {
            Result result;
            result.word = i;

            // check user word
            if( mpWord[i]->weight & SpellDictionary::USER_WORD_MASK )
            {
                if( nDistance != 0 )
                    continue;
            }
            else if( mpSpellDictionary->mbOnlyUserWord == true )
            {
                continue;
            }

            // set weight for language
            if( nLanguage == mpWord[i]->language )
                result.weight = 2 << (8+16);
            else if( nLanguage & mpWord[i]->language )
                result.weight = 1 << (8+16);
            else
                result.weight = 0 << (8+16);

            // set weight for distance
            result.weight |= (mnMinDistance-nDistance) << (16);

            // set weight for word
            result.weight |= mpWord[i]->weight;

            // append result
            pResult->push_back(result);
        }
    }

    // sort by weight
    sort(pResult->begin(), pResult->end(), result_compare);

    // free
    DELETE_ARRAY( mpNode1 );
    DELETE_ARRAY( mpNode2 );
    DELETE_ARRAY( pSequence );

    return (int)pResult->size();
}

int SpellCorrector::edits(const char* pIncorrectWord, unsigned int nWordId)
{
    unsigned int i;

    // set working & store node
    Node*        pWorkingNode = mpNode1;
    unsigned int nWorkingSize = 0;
    Node*        pTransNode   = mpNode2;
    unsigned int nTransSize   = 0;

    // init nodes
    pWorkingNode[0].x        = 0;
    pWorkingNode[0].y        = 0;
    pWorkingNode[0].word     = nWordId;
    pWorkingNode[0].distance = 0;
    nWorkingSize = 1;
    if( lowestNode(pIncorrectWord, &(pWorkingNode[0])) == true )
        return pWorkingNode[0].distance;

    // main loop
    int nLoop;
    Node* pNode;
    for( nLoop = 0; nLoop < mnMinDistance; nLoop++ )
    {
        nTransSize = 0;

        for( i = 0; i < nWorkingSize; i++ )
        {
            // set node
            pNode = &(pWorkingNode[i]);

            //if( pNode->distance > nEditsDistance )
            //    continue;

            // insert
            if( pIncorrectWord[pNode->x] )
            {
                Node node = *pNode;
                node.x++;
                node.distance++;

                if( lowestNode(pIncorrectWord, &node) == true )
                    return node.distance;

                pTransNode[nTransSize] = node;
                nTransSize++;
            }

            // delete
            if( mpWord[pNode->word]->sequence[pNode->y] )
            {
                Node node = *pNode;
                node.y++;
                node.distance++;

                if( lowestNode(pIncorrectWord, &node) == true )
                    return node.distance;

                pTransNode[nTransSize] = node;
                nTransSize++;
            }

            // change
            if( pIncorrectWord[pNode->x] &&
                mpWord[pNode->word]->sequence[pNode->y] )
            {
                Node node = *pNode;
                node.x++;
                node.y++;
                node.distance++;

                if( lowestNode(pIncorrectWord, &node) == true )
                    return node.distance;

                pTransNode[nTransSize] = node;
                nTransSize++;
            }
        }

        // swap
        swap(pWorkingNode, pTransNode);
        swap(nWorkingSize, nTransSize);
    }

    return -1;
}

bool SpellCorrector::lowestNode(const char* pIncorrectWord, Node* pNode)
{
    while( pIncorrectWord[pNode->x] &&
           mpWord[pNode->word]->sequence[pNode->y] )
    {
        if( pIncorrectWord[pNode->x] ==
            mpWord[pNode->word]->sequence[pNode->y] )
        {
            pNode->x++;
            pNode->y++;
        }
        else
            return false;
    }

    if( pIncorrectWord[pNode->x] ||
        mpWord[pNode->word]->sequence[pNode->y] )
    {
        return false;
    }

    return true;
}

} // namespace speller
