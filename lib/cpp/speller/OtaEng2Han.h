#ifndef __SPELL_OTA_ENG2HAN__
#define __SPELL_OTA_ENG2HAN__

#include "WCharString.h"

#include "OtaInterface.h"

namespace speller {

class OtaEng2Han : public OtaInterface
{
public:
    virtual ~OtaEng2Han()
    {
    }

    virtual const spr::WChar* correct(const spr::WChar* pWord);

public:
    enum {
        CHOSUNG = 0,
        JUNGSUNG = 1,
        JONGSUNG = 2
    };

protected:
    int getCode(int type, spr::WChar firstChar, spr::WChar secondChar = 0);
    int getSingleMedial(const spr::WChar* word, int length, int i);
    int getDoubleMedial(const spr::WChar* word, int length, int i);
    int getSingleFinal(const spr::WChar* word, int length, int i);
    int getDoubleFinal(const spr::WChar* word, int length, int i);

protected:
    spr::WCharString output_;
};

} // namespace speller

#endif // __SPELL_OTA_ENG2HAN__
