#ifndef __SPELLER_SPELLFACTORY__
#define __SPELLER_SPELLFACTORY__

// spr
#include "Singleton.h"
using namespace spr;

// la
#include "LAFactory.h"
#include "LADictionary.h"
using namespace la;

#include "SpellDictionary.h"
#include "SpellCorrector.h"

namespace speller {

class SpellLAKnowledge : public LAKnowledge
{
public:
    SpellLAKnowledge()
    {
        mpSpellDictionary = 0;
    }

    virtual ~SpellLAKnowledge()
    {
        DELETE_SINGLE( mpSpellDictionary );
    }

public:
    SpellDictionary* mpSpellDictionary;
};

class SpellLADictionary : public LADictionary
{
public:
    SpellLADictionary(const char* pDictionary, bool bOnlyUserWord, const char* pOtaClass);

    ~SpellLADictionary()
    {
        unloadAll();
    }

protected:
    virtual LAKnowledge* loadKnowledge(int nTarget);
    virtual void unloadKnowledge(LAKnowledge** pLAKnowledge);

protected:
    bool mbOnlyUserWord;
    const char* mpOtaClass;
};

class SpellCorrectorLA
{
public:
    SpellCorrectorLA(SpellLADictionary* pSpellLADictionary);

    int suggestWord(const char* pIncorrectWord, char**& pSuggestWord, int nEncoding, int nSuggestCount = 1);
    int suggestSentence(const char* pIncorrectSentence, char*& pSuggestSentence, int nEncoding);

protected:
    SpellLADictionary* mpSpellLADictionary;
};

class SpellFactory : public Singleton<SpellFactory>
{
public:
    int registerSpellDictionary(const char* pDictionary, bool bOnlyUserWord, const char* pOtaClass);

    SpellLADictionary* getSpellDictionary(void);
};

} // namespace speller

#endif // __SPELLER_SPELLFACTORY__
