'use strict';

function boolText() {
    return function(boolValue) {
        if (boolValue === true)
            return "是";
        else
            return "否";
    };
}

function sexText() {
    return function(sex) {
        if (sex == 'MALE') {
            return '男';
        } else if (sex == 'FEMALE') {
            return '女';
        } else {
            return '未设置';
        }
    };
}

function characters() {
    return function(input, chars, breakOnWord) {
        if (isNaN(chars)) {
            return input;
        }
        if (chars <= 0) {
            return '';
        }
        if (input && input.length > chars) {
            input = input.substring(0, chars);

            if (!breakOnWord) {
                var lastspace = input.lastIndexOf(' ');
                // Get last space
                if (lastspace !== -1) {
                    input = input.substr(0, lastspace);
                }
            } else {
                while (input.charAt(input.length - 1) === ' ') {
                    input = input.substr(0, input.length - 1);
                }
            }
            return input + '...';
        }
        return input;
    };
}

function words(input, words) {
    return function(input, words) {
        if (isNaN(words)) {
            return input;
        }
        if (words <= 0) {
            return '';
        }
        if (input) {
            var inputWords = input.split(/\s+/);
            if (inputWords.length > words) {
                input = inputWords.slice(0, words).join(' ') + '...';
            }
        }
        return input;
    };
}

angular.module('Common.utils')
    .filter('sexText', sexText)
    .filter('boolText', boolText)
    .filter('characters', characters)
    .filter('words', words);
