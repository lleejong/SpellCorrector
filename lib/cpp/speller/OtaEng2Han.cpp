#include "OtaEng2Han.h"
#include "Unicode.h"

namespace speller {

static spr::WChar NULL_STRING[2] = {
    0,
    0,
};

static spr::WChar FST_CODE[20] = {
    'r',
    'R',
    's',
    'e',
    'E',
    'f',
    'a',
    'q',
    'Q',
    't',
    'T',
    'd',
    'w',
    'W',
    'c',
    'z',
    'x',
    'v',
    'g',
      0
};
//    String init = "rRseEfaqQtTdwWczxvg";

static struct _MID_CODE
{
    spr::WChar code[3];
} MID_CODE[] = {
    {'k',  0,  0},
    {'o',  0,  0},
    {'i',  0,  0},
    {'O',  0,  0},
    {'j',  0,  0},
    {'p',  0,  0},
    {'u',  0,  0},
    {'P',  0,  0},
    {'h',  0,  0},
    {'h', 'k', 0},
    {'h', 'o', 0},
    {'h', 'l', 0},
    {'y',  0,  0},
    {'n',  0,  0},
    {'n', 'j', 0},
    {'n', 'p', 0},
    {'n', 'l', 0},
    {'b',  0,  0},
    {'m',  0,  0},
    {'m', 'l', 0},
    {'l',  0,  0},
    {  0,  0,  0}
//    String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
};

static struct _FIN_CODE
{
    spr::WChar code[3];
} FIN_CODE[] = {
    {'r',  0,  0},
    {'R',  0,  0},
    {'r', 't', 0},
    {'s',  0,  0},
    {'s', 'w', 0},
    {'s', 'g', 0},
    {'e',  0,  0},
    {'f',  0,  0},
    {'f', 'r', 0},
    {'f', 'a', 0},
    {'f', 'q', 0},
    {'f', 't', 0},
    {'f', 'x', 0},
    {'f', 'v', 0},
    {'f', 'g', 0},
    {'a',  0,  0},
    {'q',  0,  0},
    {'q', 't', 0},
    {'t',  0,  0},
    {'T',  0,  0},
    {'d',  0,  0},
    {'w',  0,  0},
    {'c',  0,  0},
    {'z',  0,  0},
    {'x',  0,  0},
    {'v',  0,  0},
    {'g',  0,  0},
    {  0,  0,  0}
//    String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
};

const spr::WChar* OtaEng2Han::correct(const spr::WChar* word)
{
    // reset
    output_ = NULL_STRING;

    int length = unicode::ucs2_strlen(word);
    bool success = true;

    int initialCode = 0, medialCode = 0, finalCode = 0;
    int tempMedialCode, tempFinalCode;

    for( int i = 0; i < length; i++ )
    {
        initialCode = medialCode = finalCode = 0;

        // get initial code
        initialCode = getCode(OtaEng2Han::CHOSUNG, word[i]);
        i++; // next

        // get medial code
        tempMedialCode = getDoubleMedial(word, length, i);

        if( tempMedialCode != -1 )
        {
            medialCode = tempMedialCode;
            i += 2;
        }
        else
        {
            medialCode = getSingleMedial(word, length, i);
            i++;
        }

        // get final code
        tempFinalCode = getDoubleFinal(word, length, i);

        if( tempFinalCode != -1 )
        {
            finalCode = tempFinalCode;

            // get next medial code
            tempMedialCode = getSingleMedial(word, length, i+2);
            if( tempMedialCode != -1 )
            {
                finalCode = getSingleFinal(word, length, i);
            }
            else
            {
                i++;
            }
        }
        else
        {
            tempMedialCode = getSingleMedial(word, length, i+1);

            if( tempMedialCode != -1 )
            {
                finalCode = 0;
                i--;     
            }
            else
            {
                finalCode = getSingleFinal(word, length, i);
                if( finalCode == -1 )
                    finalCode = 0;
            }
        }

        if( initialCode == -1 || medialCode == -1 )
        {
            success = false;
            break;
        }

        output_.append( 0xAC00 + initialCode + medialCode + finalCode );
    }

    if( success == false )
        return 0;

    return output_.w_str();
}

//            // 추출한 초성 문자 코드, 중성 문자 코드, 종성 문자 코드를 합한 후 변환하여 스트링버퍼에 넘김
//            sb.append((char)(0xAC00 + initialCode + medialCode + finalCode));
//        }  
//        return sb.toString();
//    }

int OtaEng2Han::getCode(int type, spr::WChar firstChar, spr::WChar secondChar)
{
//    // 초성
//    String init = "rRseEfaqQtTdwWczxvg";
//    // 중성
//    String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
//    // 종성
//    String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
    int i;
    spr::WChar fschar[3] = {firstChar, secondChar, 0};

    switch( type )
    {
    case OtaEng2Han::CHOSUNG :
        for( i = 0; FST_CODE[i] > 0; i++ )
        {
            if( FST_CODE[i] == fschar[0] )
            {
                return i * 21 * 28;
            }
        }
        break;
    case OtaEng2Han::JUNGSUNG :
        for( i = 0; MID_CODE[i].code[0] > 0; i++ )
        {
            if( (MID_CODE[i].code[0] == fschar[0]) &&
                (MID_CODE[i].code[1] == fschar[1]) )
            {
                return i * 28;
            }
        }
        break;
    case OtaEng2Han::JONGSUNG :
        for( i = 0; FIN_CODE[i].code[0] > 0; i++ )
        {
            if( (FIN_CODE[i].code[0] == fschar[0]) &&
                (FIN_CODE[i].code[1] == fschar[1]) )
            {
                return i + 1;
            }
        }
        break;
    default:
        break;
    }

    return -1;
}

int OtaEng2Han::getSingleMedial(const spr::WChar* word, int length, int i)
{
    if( (i+1) <= length )
    {
        return getCode(OtaEng2Han::JUNGSUNG, word[i]);
    }
    else
    {
        return -1;
    }
}

int OtaEng2Han::getDoubleMedial(const spr::WChar* word, int length, int i)
{
    int result;
    if( (i+2) > length)
    {
        return -1;
    }
    else
    {
        result = getCode(OtaEng2Han::JUNGSUNG, word[i], word[i+1]);
        if( result != -1 )
        {
            return result;
        }
        else
        {
            return -1;
        }
    }
}

int OtaEng2Han::getSingleFinal(const spr::WChar* word, int length, int i)
{
    if( (i+1) <= length )
    {
        return getCode(OtaEng2Han::JONGSUNG, word[i]);
    }
    else
    {
        return -1;
    }
}

int OtaEng2Han::getDoubleFinal(const spr::WChar* word, int length, int i)
{
    if( (i+2) > length)
    {
        return -1;
    }
    else
    {
        return getCode(OtaEng2Han::JONGSUNG, word[i], word[i+1]);
    }
} 

} // namespace speller

// package test;
// import java.io.UnsupportedEncodingException;
// public class TempTest {
//  // 코드타입 - 초성, 중성, 종성
//  enum CodeType{chosung, jungsung, jongsung}
//  
//  public static void main(String[] args) throws UnsupportedEncodingException {
//   TempTest ts1 = new TempTest();
//   String result = ts1.engToKor("zjavbxjtkTek");
//   System.out.println(result);
//  }
//  
//  /** 
//    * 영어를 한글로... 
//    */ 
//  public String engToKor(String eng){
//   StringBuffer sb = new StringBuffer();
//   int initialCode = 0, medialCode = 0, finalCode = 0;
//   int tempMedialCode, tempFinalCode;
//   
//   for(int i = 0; i < eng.length(); i++){
//    // 초성코드 추출
//    initialCode = getCode(CodeType.chosung, eng.substring(i, i+1));   
//    i++; // 다음문자로
//    
//    // 중성코드 추출
//    tempMedialCode = getDoubleMedial(i, eng);   // 두 자로 이루어진 중성코드 추출
//    
//    if(tempMedialCode != -1){       
//     medialCode = tempMedialCode;
//     i += 2;
//    }else{            // 없다면,
//     medialCode = getSingleMedial(i, eng);   // 한 자로 이루어진 중성코드 추출
//     i++;
//    }
//    
//    // 종성코드 추출
//    tempFinalCode = getDoubleFinal(i, eng);    // 두 자로 이루어진 종성코드 추출    
//    if(tempFinalCode != -1){
//     finalCode = tempFinalCode;
//     // 그 다음의 중성 문자에 대한 코드를 추출한다. 
//     tempMedialCode = getSingleMedial(i+2, eng);
//     if( tempMedialCode != -1 ){      // 코드 값이 있을 경우 
//      finalCode = getSingleFinal(i, eng);   // 종성 코드 값을 저장한다.
//              }else{
//               i++;
//              }
//    }else{            // 코드 값이 없을 경우 ,
//     tempMedialCode = getSingleMedial(i+1, eng);  // 그 다음의 중성 문자에 대한 코드 추출. 
//     if(tempMedialCode != -1){      // 그 다음에 중성 문자가 존재할 경우,     
//      finalCode = 0;        // 종성 문자는 없음.
//      i--;     
//     }else{
//      finalCode = getSingleFinal(i, eng);   // 종성 문자 추출
//      if( finalCode == -1 )
//       finalCode = 0;
//     }
//    }
//    // 추출한 초성 문자 코드, 중성 문자 코드, 종성 문자 코드를 합한 후 변환하여 스트링버퍼에 넘김
//    sb.append((char)(0xAC00 + initialCode + medialCode + finalCode));
//   }  
//   return sb.toString();
//  }
//  
//  /** 
//    * 해당 문자에 따른 코드를 추출한다. 
//    * @param type 초성 : chosung, 중성 : jungsung, 종성 : jongsung 구분 
//    * @param char 해당 문자 
//    */ 
//  private int getCode(CodeType type, String c){
//   // 초성
//   String init = "rRseEfaqQtTdwWczxvg";
//   // 중성
//   String[] mid = {"k","o","i","O","j","p","u","P","h","hk", "ho","hl","y","n","nj","np", "nl", "b", "m", "ml", "l"};
//   // 종성
//   String[] fin = {"r", "R", "rt", "s", "sw", "sg", "e", "f", "fr", "fa", "fq", "ft", "fx", "fv", "fg", "a", "q", "qt", "t", "T", "d", "w", "c", "z", "x", "v", "g"};
//   
//   switch(type){
//    case chosung :
//     int index = init.indexOf(c);
//     if( index != -1 ){
//      return index * 21 * 28;
//     }
//     break;
//    case jungsung :
//     
//     for(int i = 0; i < mid.length; i++){
//      if(mid[i].equals(c)){
//        return i * 28;
//       }
//     }
//     break;
//    case jongsung :
//     for(int i = 0; i < fin.length; i++){
//      if(fin[i].equals(c)){
//        return i + 1;
//       }
//     }
//     break;
//    default:
//     System.out.println("잘못된 타입 입니다");
//   }
//   
//   return -1;
//  }
//  
//  // 한 자로 된 중성값을 리턴한다
//  // 인덱스를 벗어낫다면 -1을 리턴
//  private int getSingleMedial(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jungsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//  
//  // 두 자로 된 중성을 체크하고, 있다면 값을 리턴한다.
//  // 없으면 리턴값은 -1
//  private int getDoubleMedial(int i, String eng){
//   int result;
//   if((i+2) > eng.length()){
//    return -1;
//   }else{
//    result = getCode(CodeType.jungsung, eng.substring(i, i+2));
//    if(result != -1){
//     return result;
//    }else{
//     return -1;
//    }
//   }
//  }
//  
//  // 한 자로된 종성값을 리턴한다
//  // 인덱스를 벗어낫다면 -1을 리턴
//  private int getSingleFinal(int i, String eng){
//   if((i+1) <= eng.length()){
//    return getCode(CodeType.jongsung, eng.substring(i, i+1));
//   }else{
//    return -1;
//   }
//  }
//  
//  // 두 자로된 종성을 체크하고, 있다면 값을 리턴한다.
//  // 없으면 리턴값은 -1
//  private int getDoubleFinal(int i, String eng){
//   if((i+2) > eng.length()){
//    return -1;
//   }else{
//    return getCode(CodeType.jongsung, eng.substring(i, i+2));
//   }
//  }
// } 
