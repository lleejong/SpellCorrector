#include "OtaInterface.h"
#include "OtaEng2Han.h"

namespace speller {

OtaInterface* OtaInterface::create(const char* pOtaClass)
{
    OtaInterface* inter = 0x00;
    if( strcmp(pOtaClass, "eng2han") == 0 )
        inter = new OtaEng2Han();
    return inter;
}

} // namespace speller
