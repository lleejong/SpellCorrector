##################################################
# include c-compiler environment makefile
##################################################
ifeq ($(origin SF1_SRC), undefined)
$(error no environment variable SF1_SRC))
else
include $(SF1_SRC)/build_system/common-defs.mak
endif

##################################################
# definition for target
##################################################
OBJS = SpellCorrector.o \
	   SpellDictionary.o \
	   SpellFactory.o \
	   SpellTable.o \
	   OtaInterface.o \
	   OtaEng2Han.o

INCS = SpellCorrector.h \
	   SpellDictionary.h \
	   SpellFactory.h \
	   SpellTable.h \
	   OtaInterface.h \
	   OtaEng2Han.h

LIB_OUT = libspeller.a

INC_DIRS  = -I. \
            -I$(SRC_DIR) \
            -I$(LIB_SRC_DIR)/sflogger \
            -I$(LIB_SRC_DIR)/spr \
            -I$(LIB_SRC_DIR)/unicode \
            -I$(LIB_SRC_DIR)/mon \
            -I$(LIB_SRC_DIR)/rgl \
            -I$(LIB_SRC_DIR)/stemmer \
            -I$(LIB_SRC_DIR)/li \
            -I$(LIB_SRC_DIR)/la \
			-I$(LIB_SRC_DIR)/la/wisekma-orange/src \
			-I$(LIB_SRC_DIR)/la/wisekma-orange/src/interface
#			-I$(LIB_SRC_DIR)/la/wisekma-blue/src/ \
			-I$(LIB_SRC_DIR)/la/wisekma-blue/src/ma \
			-I$(LIB_SRC_DIR)/la/wisekma-blue/src/include

ifdef SF1_EXTEND_ENGLISH
INC_DIRS += -I$(LIB_SRC_DIR)/la/ema-v2/src \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/include \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/ma \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/util \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/wpl \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/tagger \
            -I$(LIB_SRC_DIR)/la/ema-v2/src/tagger/maxent
endif

ifdef SF1_EXTEND_JAPANESE
INC_DIRS += -I$(LIB_SRC_DIR)/la/ijma-v1/include \
		    -I$(LIB_SRC_DIR)/la/ijma-v1/source/include \
		    -I$(LIB_SRC_DIR)/la/ijma-v1/source/libmecab
endif

ifdef SF1_EXTEND_CHINESE
INC_DIRS += -I$(LIB_SRC_DIR)/la/icma-v1/include
endif

LIB_DIRS = -L$(LIB_SRC_DIR)/sflogger \
           -L$(LIB_SRC_DIR)/spr \
           -L$(LIB_SRC_DIR)/unicode \
           -L$(LIB_SRC_DIR)/mon \
           -L$(LIB_SRC_DIR)/rgl \
           -L$(LIB_SRC_DIR)/stemmer \
           -L$(LIB_SRC_DIR)/li \
           -L$(LIB_SRC_DIR)/la 

EXE_LIBS = -lsflogger -lspr -lunicode -lmon -lrgl -lstemmer -lla -lli

##################################################
# definition for test
##################################################
TEST_SOURCE = $(shell ls t_*.cpp)
TEST_OBJS   = $(TEST_SOURCE:.cpp=.o)
TEST_EXE    = $(TEST_SOURCE:.cpp=)

##################################################
# suffixes rules
##################################################
.SUFFIXES: .cpp .c .o .d

##################################################
# all
##################################################
all: clean-out $(LIB_OUT)

##################################################
# library output
##################################################
$(LIB_OUT): $(OBJS)
	$(LIB_PROG) $(LIB_FLAGS) $@ $?

##################################################
# test
##################################################
test: clean-test $(TEST_EXE)

t_%: t_%.o $(LIB_OUT)
	$(EXE_PROG) $(EXE_FLAGS) $(EXE_CON_FLAGS) $(LP_FLAGS) -o $@ $? $(LIB_DIRS) $(EXE_LIBS)

##################################################
# automatic dependency
##################################################
ifneq ($(MAKECMDGOALS),clean)
include $(OBJS:.o=.d)
include $(TEST_OBJS:.o=.d)
endif

##################################################
# install
##################################################
install: install-library install-dictionary

install-library:
	@$(MKD_PROG) $(INC_DIR)
	@$(MKD_PROG) $(LIB_DIR)
	@echo "[+] install speller library: $(LIB_OUT)"
	@($(CPY_PROG) $(LIB_OUT) $(LIB_DIR)) || exit 1
	@echo "[+] install speller header:"
	@for header in $(INCS); do\
		echo "    + $$header"; \
		($(CPY_PROG) $$header $(INC_DIR)) || exit 1; \
	done

install-dictionary:
	@$(MKD_PROG) $(DEV_DIR)/knowledge/speller/0
	@echo "[+] install speller dictionary:"
	@$(CPY_PROG) speller.utf8 $(DEV_DIR)/knowledge/speller/0

##################################################
# compile
##################################################
%.o: %.cpp
	$(CC_PROG) $(CC_FLAGS) $(INC_DIRS) -o $@ $<

##################################################
# generate dependency > *.d
##################################################
%.d: %.cpp
	@gcc -MM $(INC_DIRS) $< > $@

##################################################
# clean
##################################################
clean: clean-out clean-test
	$(DEL_PROG) $(OBJS) $(OBJS:.o=.d)
	$(DEL_PROG) $(TEST_OBJS) $(TEST_OBJS:.o=.d)
clean-out:
	$(DEL_PROG) $(LIB_OUT)
clean-test:
	$(DEL_PROG) $(TEST_EXE)

##################################################
# include c-compiler environment makefile
##################################################
include $(SF1_SRC)/build_system/common-rules.mak
