// speller
#include "SpellCorrector.h"
#include <time.h>
#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <vector>
using namespace speller;
using namespace std;

namespace patch{
    template < typename T > std::string to_string(const T& n){
        std::ostringstream stm;
        stm << n;
        return stm.str();
    }
}


int main(void)
{
    const bool ONLY_OTA_EXIST = true;
    const bool EVERY_CORRECT_WORD = false;
    SpellDictionary dic(EVERY_CORRECT_WORD,"n");
    dic.load("./");

    SpellCorrector corrector(&dic, 2);
    string s[] = {"result_oC", "result_tCD", "result_tCI", "result_oV", "result_tVD", "result_tVI"};
	clock_t begin, end;
	clock_t time=0;

    vector<string> output;

	for(int i = 0; i < 6; i++){
		ifstream in(patch::to_string(s[i]+".txt").c_str());
        cout<<s[i]<<" Start"<<endl;
		output.push_back(s[i]);
		char line[1024];

		begin = clock();

		if(in.is_open()){
			int totalCnt = 0;
			int corrCnt = 0;

			while(!in.eof()){
				in.getline(line,1024);
				vector<char*> spl;
				char* current = strtok(line,"\t");
				while(current!=NULL){
					spl.push_back(current);
					current = strtok(NULL, "\t");
				}
				if(spl.size() == 0)
					continue;
				char* label = spl[0];
				char* query = spl[1];
                char* result;
                int   exist;
                exist = corrector.suggestSentence(query, result, CHARSET::UNICODE_CP949);
                // print and free
                if( exist )
                {
                    //printf("# [%s]\n", result);
                    WChar* resultWchar = Unicode::convertMBStoWCS(result, CHARSET::UNICODE_CP949);
                    result = SpellTable::getInstance()->convert2KeyboardSequence(resultWchar);
                    if(strcmp(label, result) == 0)
                        corrCnt++;
                    DELETE_ARRAY(result);
                }
                totalCnt++;
                if(totalCnt % 1000 == 0){
                    std::cout<<totalCnt + "...";
                }
			}
			in.close();
			end = clock();
            cout<<s[i]<<" End"<<endl;
			clock_t elapsedTime = (end - begin);
			output.push_back("====================");
            output.push_back("Total : " + patch::to_string(totalCnt));
            output.push_back("Correct : " + patch::to_string(corrCnt));
            output.push_back("Accuracy : " + patch::to_string((double) corrCnt / totalCnt * 100));
            output.push_back("Elapsed Time : " + patch::to_string((double) elapsedTime/1000.0));
            output.push_back("====================");
            cout<<"Elpased Time : " << elapsedTime << + " sec."<<endl;
            time += elapsedTime;
		}
		else{
			std::cout<<"ERROR" <<endl;
		}

	}
	output.push_back("Total Elapsed Time : " + patch::to_string((double)time/1000.0));
    cout << "Total Elapsed Time : " << patch::to_string((double)time/1000.0) << endl;

	ofstream out("result.txt");
	for(int i = 0; i < output.size(); i++){
		out << output[i].c_str() << endl;
	}
	out.close();
	std::cout << "END"<<endl;

    return 0;
}
