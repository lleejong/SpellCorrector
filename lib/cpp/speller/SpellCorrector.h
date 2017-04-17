#ifndef __SPELLER_SPELLCORRECTOR__
#define __SPELLER_SPELLCORRECTOR__

#include <math.h>

// std
#include <vector>
using namespace std;

// unicode
#include "UniCharts.h"
using namespace unicode;

#include "SpellDictionary.h"
#include "OtaInterface.h"

namespace speller {

class SpellCorrector
{
public:
    struct Node
    {
        unsigned int x;
        unsigned int y;
        unsigned int word;
        unsigned int distance;

        Node() : x(0), y(0), word(0), distance(0)
        { }
    };

    struct Result
    {
        unsigned int word;
        unsigned int weight;
    };

public:
    SpellCorrector(SpellDictionary* pSpellDictionary, int nMinDistance);

    virtual ~SpellCorrector();

    int suggestWord(const char* pIncorrectWord, char**& pSuggestWord, int nEncoding, int nSuggestCount = 1);

    int suggestSentence(const char* pIncorrectSentence, char*& pSuggestSentence, int nEncoding);

protected:
    int suggest(const WChar* pIncorrectWord, vector<Result>* pResult);
    int edits(const char* pIncorrectWord, unsigned int nWordId);
    bool lowestNode(const char* pIncorrectWord, Node* pNode);

protected:
    SpellDictionary* mpSpellDictionary;
    SpellDictionary::Word** mpWord;
    unsigned int mnDictionarySize;
    int mnMinDistance;
    unsigned int mnMaxNode;
    Node* mpNode1;
    Node* mpNode2;
    OtaInterface* mpOta;
};

} // namespace speller

#endif // __SPELLER_SPELLCORRECTOR__
