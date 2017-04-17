// speller
#include "SpellFactory.h"
using namespace speller;

int main(void)
{
    SpellFactory::getInstance()->registerSpellDictionary("./");

    return 0;
}
