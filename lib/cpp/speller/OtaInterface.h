#ifndef __SPELLER_OTA_INTERFACE__
#define __SPELLER_OTA_INTERFACE__

#include "SprTypes.h"

namespace speller {

class OtaInterface
{
public:
    static OtaInterface* create(const char* pOtaClass);

public:
    virtual ~OtaInterface()
    {
    }

    virtual const spr::WChar* correct(const spr::WChar* pWord) = 0;
};

} // namespace speller

#endif //  __SPELLER_OTA_INTERFACE__
