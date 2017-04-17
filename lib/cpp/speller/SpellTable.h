#ifndef __SPELLER_SPELLTABLE__
#define __SPELLER_SPELLTABLE__

#include <string.h>

// spr
#include "Singleton.h"
using namespace spr;

namespace speller {

enum
{
    SPELLTABLE_ENGLISH = 0x00000001,
    SPELLTABLE_KOREAN  = 0x00000002,
};

typedef struct _KeyboardSequenceTable
{
    char* sequence;
    unsigned int length;
    unsigned int language;
} KeyboardSequenceTable;

typedef struct _KeyboardSequenceTableDefine
{
    unsigned int value;
    KeyboardSequenceTable table;
    unsigned int dummy; // for 8bytes alignment
} KeyboardSequenceTableDefine;

extern KeyboardSequenceTableDefine KEYBOARD_SEQUENCE_TABLE_DEFINE[];

class SpellTable : public Singleton<SpellTable>
{
public:
    SpellTable();

    char* convert2KeyboardSequence(const WChar* pWord);

    unsigned int getLanguage(const WChar* pWord);

protected:
    KeyboardSequenceTable* mpTable[0xFFFF];
};

} // namespace speller

#endif // __SPELLER_SPELLTABLE__
