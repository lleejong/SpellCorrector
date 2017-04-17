#ifndef __SPELLER_SPELLDICTIONARY__
#define __SPELLER_SPELLDICTIONARY__

#include <stdio.h>

// std
#include <algorithm>
using namespace std;

#include "ConfigConstants.h"

// spr
#include "File.h"
#include "FixedString.h"
#include "StringTokenizer.h"
using namespace spr;

// unicode
#include "Unicode.h"
using namespace unicode;

#include "SpellTable.h"

namespace speller {

const char SPELL_DICTIONARY_FILE[] = "speller.utf8";

class SpellDictionary
{
public:
    enum
    {
        USER_WORD_MASK = 0x80000000,
    };

public:
    struct Word
    {
        WChar* word;
        char*  sequence;
        unsigned int language;
        unsigned int weight;

        Word() : word(0), sequence(0),
                 language(0), weight(0) { }

        ~Word()
        {
            DELETE_ARRAY( word );
            DELETE_ARRAY( sequence );
        }
    };

public:
    SpellDictionary(bool bOnlyUserWord, const char* pOtaClass);

    virtual ~SpellDictionary();

    int load(const char* pPath);

    inline unsigned int size(void) { return mnWordSize; }

    inline Word** word(void) { return mpWordList; }

protected:
    int add(const char* pCorrect, const char* pCandidate, unsigned int nWeight);

protected:
    Word**       mpWordList;
    unsigned int mnWordSize;
    unsigned int mnWordMax;
public:
    bool mbOnlyUserWord;
    const char* mpOtaClass;
};

} // namespace speller

#endif // __SPELLER_SPELLDICTIONARY__
