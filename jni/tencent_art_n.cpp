//
// Created by Administrator on 2017/4/10.
//

#ifndef DALVIK_LOCAL_NEW_TENCENT_ART_H
#define DALVIK_LOCAL_NEW_TENCENT_ART_H

#include <android/log.h>
#include "tencent_art_n.h"
#include "art_7_0.h"

static const uint32_t kDexNoIndex = 0xFFFFFFFF;

extern "C" jint Java_com_example_patchinlinedemo_MainApplication_clearDexCache(JNIEnv* evn, jobject thiz,
		jlong methodAddress, jint numResolvedMethods, jlong classAddrress, jint numResolvedTypes) {
//    art::mirror::ArtMethod ** methodAddr = (art::mirror::ArtMethod **)methodAddress;

//    uint64_t ** methodAddr = (uint64_t **)methodAddress;

//    __android_log_print(ANDROID_LOG_DEBUG, "sssd", "clearDexCache methodAddress");
//    for (int i = 0; i < ((int)numResolvedMethods); i++) {



//        art::mirror::ArtMethod * dmeth = methodAddr[i];
//
//
//        if (dmeth->dex_method_index_ > 0 ) {
////            int classStatus = reinterpret_cast<art::mirror::Class*>(dmeth->declaring_class_)->status_;
//
//            int  classStatus = 0;
//
//            __android_log_print(ANDROID_LOG_DEBUG, "sssd", "clearDexCache method_index_ %d hotness = %d classStatus = %d",
//                                dmeth->dex_method_index_, (methodAddr[i]->hotness_count_), classStatus);
//
//        }


//        if (!((methodAddr[i])->dex_method_index_ > 0)){
//            (methodAddr[i])->dex_method_index_ = kDexNoIndex;
//        }

//        if (!methodAddr[i]->IsNative()) {
//            (methodAddr[i])->dex_method_index_ = kDexNoIndex;
//        }



//        methodAddr[i] = nullptr;



//    }
//    art::mirror::Class** classAddr = (art::mirror::Class **) classAddrress;
//    }

    uint64_t ** classAddr = (uint64_t**) classAddrress;

    for (int j = 0; j < ((int)numResolvedTypes); j++) {
//        if (classAddr[j] ) {
            //__android_log_print(ANDROID_LOG_DEBUG, "PatchInlineDemo", "clearDexCache dex_type_idx_=%d", (classAddr[j])->dex_type_idx_);
    	__android_log_print(ANDROID_LOG_DEBUG, "PatchInlineDemo", "native clearDexCache resolveTypeId=%d", j);
            *(classAddr + j) = nullptr;
//        }
    }

    return 1;
}

#endif //DALVIK_LOCAL_NEW_TENCENT_ART_H
