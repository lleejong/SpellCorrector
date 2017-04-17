#include "SpellFactory.h"

namespace speller {

SpellLADictionary::SpellLADictionary(const char* pDictionary, bool bOnlyUserWord, const char* pOtaClass) : LADictionary("", pDictionary)
{
    mbOnlyUserWord = bOnlyUserWord;
    mpOtaClass = pOtaClass;
}

LAKnowledge* SpellLADictionary::loadKnowledge(int nTarget)
{
    // build user dictionary path
    DictionaryPathType userDictionaryPath;
    if( nTarget == MAX_TARGET )
    {
        userDictionaryPath = mUserDictionaryPath;
    }
    else
    {
        userDictionaryPath = mUserDictionaryPath;
        userDictionaryPath += PATH_DELIMITER;
        userDictionaryPath += (char)(nTarget + '0');
    }

    SpellLAKnowledge* pLAKnowledge = new SpellLAKnowledge();
    if( pLAKnowledge == 0 )
        return 0;

    pLAKnowledge->mpSpellDictionary = new SpellDictionary(mbOnlyUserWord, mpOtaClass);
    if( pLAKnowledge->mpSpellDictionary == 0 )
        return 0;

    switch( pLAKnowledge->mpSpellDictionary->load(userDictionaryPath.c_str()) )
    {
    case -1:
        sflog->info(SS_LAE_INIT, SFL_12701, "speller", "Speller", userDictionaryPath.c_str());
        return 0;

    case 0:
        sflog->info(SS_LAE_INIT, SFL_12000, "Speller", userDictionaryPath.c_str());
        break;
    }

    return pLAKnowledge;
}

void SpellLADictionary::unloadKnowledge(LAKnowledge** pLAKnowledge)
{
    if( *pLAKnowledge )
    {
        DELETE_SINGLE( *pLAKnowledge );
    }
}

SpellCorrectorLA::SpellCorrectorLA(SpellLADictionary* pSpellLADictionary)
    : mpSpellLADictionary(pSpellLADictionary)
{
}

int SpellCorrectorLA::suggestWord(const char* pIncorrectWord, char**& pSuggestWord, int nEncoding, int nSuggestCount)
{
    SpellLAKnowledge* pLAKnowledge = (SpellLAKnowledge*)mpSpellLADictionary->get();

    SpellCorrector corrector(pLAKnowledge->mpSpellDictionary, 2);
    int ret = corrector.suggestWord(pIncorrectWord, pSuggestWord, nEncoding, nSuggestCount);

    mpSpellLADictionary->put(pLAKnowledge);

    return ret;
}

int SpellCorrectorLA::suggestSentence(const char* pIncorrectSentence, char*& pSuggestSentence, int nEncoding)
{
    SpellLAKnowledge* pLAKnowledge = (SpellLAKnowledge*)mpSpellLADictionary->get();

    SpellCorrector corrector(pLAKnowledge->mpSpellDictionary, 2);
    int ret = corrector.suggestSentence(pIncorrectSentence, pSuggestSentence, nEncoding);

    mpSpellLADictionary->put(pLAKnowledge);

    return ret;
}

int SpellFactory::registerSpellDictionary(const char* pDictionary, bool bOnlyUserWord, const char* pOtaClass)
{
    LADictionary* pLADictionary = (LADictionary*)new SpellLADictionary(pDictionary, bOnlyUserWord, pOtaClass);

    // loding dictionary
    if( pLADictionary->load() < 0 )
        return LAFactory::ERR_CANNOT_LOAD_DICTIONARY;

    return lafactory->registerMethodDictionary("speller", pLADictionary);
}

SpellLADictionary* SpellFactory::getSpellDictionary(void)
{
    return (SpellLADictionary*)lafactory->findMethodDictionary("speller");
}

} // namespace speller
