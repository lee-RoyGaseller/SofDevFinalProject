/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final String APP_TAG = "AnaDict";

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private static boolean OML = true;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private Set<String> wordSet = new HashSet<>();
    private Map<String, ArrayList<String >> lettersToWord = new HashMap<>();
    private Map<Integer,ArrayList<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String keyString = sortLetters(word);
            int length = word.length();
            if(sizeToWords.containsKey(length)){
                sizeToWords.get(length).add(word);
            }
            else {
                ArrayList<String> wordsOfThisLength = new ArrayList<>();
                wordsOfThisLength.add(word);
                sizeToWords.put(length,wordsOfThisLength);
            }
            if (lettersToWord.containsKey(keyString)) {
                lettersToWord.get(keyString).add(word);
            } else {
                ArrayList<String> aList = new ArrayList<>();
                aList.add(word);
                lettersToWord.put(keyString, aList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !word.contains(base));
    }

    public List<String> getAnagrams(String targetWord) {
        java.lang.String key = sortLetters(targetWord);
        int keyLen = key.length();
        ArrayList<java.lang.String> result = new ArrayList<java.lang.String>();
        for (java.lang.String word : wordList) {
            if (word.length() == keyLen) {
                if (key.equals(sortLetters(word))) { result.add(word); }
            }
        }
        return result;
    }

    public String sortLetters(String word) {
        // takes a string, turns it into a Character array, sorts, and return the array as a string.
        char temp[] = word.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            String key = word + alphabet;
            if (lettersToWord.containsKey(sortLetters(key))) {
                ArrayList<String> listAnagrams = lettersToWord.get(sortLetters(key));
                for (String anagram : listAnagrams) {
                    if (isGoodWord(word, anagram)) { result.add(anagram); }
                }

            }
        }
        return result;
    }

    public List<String> getAnagramsWithTwoMoreLetters(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            for (char alphabet2 = 'a'; alphabet2 <= 'z'; alphabet2++) {
                String key = word + alphabet + alphabet2;
                if (lettersToWord.containsKey(sortLetters(key))) {
                    ArrayList<String> listAnagrams = lettersToWord.get(sortLetters(key));
                    for (String anagram : listAnagrams) {
                        if (isGoodWord(word, anagram)) { result.add(anagram); }
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int length = sizeToWords.get(wordLength).size();
        String word = sizeToWords.get(wordLength).get(random.nextInt(length));
        int numAnagrams = lettersToWord.get(sortLetters(word)).size();
        while (numAnagrams < MIN_NUM_ANAGRAMS) {
            word = sizeToWords.get(wordLength).get(random.nextInt(length));
            numAnagrams = lettersToWord.get(sortLetters(word)).size();
        }

        //Revert to a short starter word after some time.
        if ( wordLength!=MAX_WORD_LENGTH ){ wordLength++; }
        else { wordLength = DEFAULT_WORD_LENGTH; }

        return word;
    }

    public boolean isOML() {
        return OML;
    }

    public void changeMode() {
        OML = !OML;
    }
}
