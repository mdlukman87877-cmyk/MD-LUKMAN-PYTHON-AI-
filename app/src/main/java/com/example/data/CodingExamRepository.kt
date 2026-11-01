package com.example.data

import com.example.model.CodingProblem
import com.example.model.TestCase

object CodingExamRepository {

    val problems: List<CodingProblem> by lazy {
        listOf(
            CodingProblem(
                id = 1,
                title = "Sum of Two Numbers",
                difficulty = "Easy",
                category = "Basics",
                descriptionHindi = "दो संख्याओं का योग (Sum) ज्ञात करें। यूज़र से दो संख्याएं इनपुट लें और उनका योग प्रिंट करें।",
                descriptionEnglish = "Write a program that takes two numbers as input and prints their sum.",
                inputFormat = "Two integers, one per line (e.g. 5 and 7)",
                outputFormat = "Single integer showing the sum (e.g. 12)",
                starterCode = """# Write your Python code here
a = int(input())
b = int(input())
# Print sum of a and b:
""",
                solutionCode = """a = int(input())
b = int(input())
print(a + b)
""",
                testCases = listOf(
                    TestCase(input = "5\n7", expectedOutput = "12"),
                    TestCase(input = "20\n30", expectedOutput = "50"),
                    TestCase(input = "-5\n15", expectedOutput = "10")
                )
            ),
            CodingProblem(
                id = 2,
                title = "Even or Odd Checker",
                difficulty = "Easy",
                category = "Conditionals",
                descriptionHindi = "दी गई संख्या की जाँच करें कि वह 'Even' (सम) है या 'Odd' (विषम)।",
                descriptionEnglish = "Given an integer, check whether it is Even or Odd.",
                inputFormat = "Single integer (e.g. 4)",
                outputFormat = "'Even' or 'Odd'",
                starterCode = """num = int(input())
# Check if num is Even or Odd:
""",
                solutionCode = """num = int(input())
if num % 2 == 0:
    print("Even")
else:
    print("Odd")
""",
                testCases = listOf(
                    TestCase(input = "4", expectedOutput = "Even"),
                    TestCase(input = "7", expectedOutput = "Odd"),
                    TestCase(input = "100", expectedOutput = "Even")
                )
            ),
            CodingProblem(
                id = 3,
                title = "Reverse a String",
                difficulty = "Easy",
                category = "Strings",
                descriptionHindi = "दिए गए शब्द को उल्टा (Reverse) करके प्रिंट करें।",
                descriptionEnglish = "Reverse the given input string and print the reversed text.",
                inputFormat = "A single line string (e.g. python)",
                outputFormat = "Reversed string (e.g. nohtyp)",
                starterCode = """text = input()
# Reverse the string and print:
""",
                solutionCode = """text = input()
print(text[::-1])
""",
                testCases = listOf(
                    TestCase(input = "python", expectedOutput = "nohtyp"),
                    TestCase(input = "lukman", expectedOutput = "namkul"),
                    TestCase(input = "hello", expectedOutput = "olleh")
                )
            ),
            CodingProblem(
                id = 4,
                title = "Largest of Three Numbers",
                difficulty = "Medium",
                category = "Conditionals",
                descriptionHindi = "तीन संख्याओं में से सबसे बड़ी संख्या (Max) ढूंढें।",
                descriptionEnglish = "Find the maximum of three given numbers.",
                inputFormat = "Three integers on separate lines (e.g. 10, 45, 23)",
                outputFormat = "The maximum number (e.g. 45)",
                starterCode = """a = int(input())
b = int(input())
c = int(input())
# Find and print the largest number:
""",
                solutionCode = """a = int(input())
b = int(input())
c = int(input())
if a >= b and a >= c:
    print(a)
elif b >= a and b >= c:
    print(b)
else:
    print(c)
""",
                testCases = listOf(
                    TestCase(input = "10\n45\n23", expectedOutput = "45"),
                    TestCase(input = "99\n12\n3", expectedOutput = "99"),
                    TestCase(input = "5\n5\n10", expectedOutput = "10")
                )
            ),
            CodingProblem(
                id = 5,
                title = "Factorial of a Number",
                difficulty = "Medium",
                category = "Loops",
                descriptionHindi = "किसी संख्या का Factorial ज्ञात करें (e.g., 5! = 5 * 4 * 3 * 2 * 1 = 120)।",
                descriptionEnglish = "Calculate the factorial of a non-negative integer n.",
                inputFormat = "Single integer n (e.g. 5)",
                outputFormat = "Factorial value (e.g. 120)",
                starterCode = """n = int(input())
fact = 1
# Calculate factorial using a loop:
""",
                solutionCode = """n = int(input())
fact = 1
for i in range(1, n + 1):
    fact *= i
print(fact)
""",
                testCases = listOf(
                    TestCase(input = "5", expectedOutput = "120"),
                    TestCase(input = "4", expectedOutput = "24"),
                    TestCase(input = "3", expectedOutput = "6")
                )
            ),
            CodingProblem(
                id = 6,
                title = "Check Palindrome",
                difficulty = "Medium",
                category = "Strings",
                descriptionHindi = "जाँचें कि दिया गया शब्द Palindrome है या नहीं (उल्टा और सीधा एक जैसा, e.g., 'radar', 'madam')।",
                descriptionEnglish = "Check if the given word is a Palindrome. Print 'Yes' or 'No'.",
                inputFormat = "A single word string",
                outputFormat = "'Yes' if palindrome, else 'No'",
                starterCode = """word = input()
# Check palindrome:
""",
                solutionCode = """word = input()
if word == word[::-1]:
    print("Yes")
else:
    print("No")
""",
                testCases = listOf(
                    TestCase(input = "radar", expectedOutput = "Yes"),
                    TestCase(input = "python", expectedOutput = "No"),
                    TestCase(input = "madam", expectedOutput = "Yes")
                )
            ),
            CodingProblem(
                id = 7,
                title = "Vowel Counter",
                difficulty = "Medium",
                category = "Loops & Strings",
                descriptionHindi = "दिए गए वाक्य में स्वरों (Vowels: a, e, i, o, u) की कुल संख्या गिनें।",
                descriptionEnglish = "Count the total number of vowels in a string (case-insensitive).",
                inputFormat = "A string of text",
                outputFormat = "Integer count of vowels",
                starterCode = """text = input().lower()
vowels = "aeiou"
count = 0
# Count vowels and print:
""",
                solutionCode = """text = input().lower()
vowels = "aeiou"
count = 0
for ch in text:
    if ch in vowels:
        count += 1
print(count)
""",
                testCases = listOf(
                    TestCase(input = "apple", expectedOutput = "2"),
                    TestCase(input = "lukman", expectedOutput = "2"),
                    TestCase(input = "education", expectedOutput = "5")
                )
            ),
            CodingProblem(
                id = 8,
                title = "FizzBuzz Challenge",
                difficulty = "Medium",
                category = "Conditionals",
                descriptionHindi = "यदि संख्या 3 से विभाज्य है तो 'Fizz', 5 से है तो 'Buzz', और दोनों से है तो 'FizzBuzz' प्रिंट करें। अन्यथा वही संख्या प्रिंट करें।",
                descriptionEnglish = "Print Fizz if divisible by 3, Buzz if by 5, FizzBuzz if both, else the number.",
                inputFormat = "Single integer",
                outputFormat = "'Fizz', 'Buzz', 'FizzBuzz', or number",
                starterCode = """n = int(input())
# Write FizzBuzz logic:
""",
                solutionCode = """n = int(input())
if n % 3 == 0 and n % 5 == 0:
    print("FizzBuzz")
elif n % 3 == 0:
    print("Fizz")
elif n % 5 == 0:
    print("Buzz")
else:
    print(n)
""",
                testCases = listOf(
                    TestCase(input = "15", expectedOutput = "FizzBuzz"),
                    TestCase(input = "9", expectedOutput = "Fizz"),
                    TestCase(input = "10", expectedOutput = "Buzz"),
                    TestCase(input = "7", expectedOutput = "7")
                )
            ),
            CodingProblem(
                id = 9,
                title = "Sum of Digits",
                difficulty = "Hard",
                category = "Loops",
                descriptionHindi = "किसी संख्या के सभी अंकों का योग (Sum of Digits) ज्ञात करें (e.g. 123 -> 1 + 2 + 3 = 6)।",
                descriptionEnglish = "Find the sum of digits of a given positive integer.",
                inputFormat = "Single integer (e.g. 1234)",
                outputFormat = "Sum of digits (e.g. 10)",
                starterCode = """n = int(input())
digit_sum = 0
# Compute sum of digits:
""",
                solutionCode = """n = int(input())
digit_sum = 0
for d in str(n):
    digit_sum += int(d)
print(digit_sum)
""",
                testCases = listOf(
                    TestCase(input = "123", expectedOutput = "6"),
                    TestCase(input = "1234", expectedOutput = "10"),
                    TestCase(input = "999", expectedOutput = "27")
                )
            ),
            CodingProblem(
                id = 10,
                title = "Multiplication Table",
                difficulty = "Easy",
                category = "Loops",
                descriptionHindi = "दी गई संख्या का 1 से 5 तक का पहाड़ा (Multiplication Table) प्रिंट करें।",
                descriptionEnglish = "Print multiplication table from 1 to 5 for given number n in format 'n x i = res'.",
                inputFormat = "Single integer (e.g. 3)",
                outputFormat = "5 lines showing n x 1 = n, etc.",
                starterCode = """n = int(input())
for i in range(1, 6):
    print(f"{n} x {i} = {n * i}")
""",
                solutionCode = """n = int(input())
for i in range(1, 6):
    print(f"{n} x {i} = {n * i}")
""",
                testCases = listOf(
                    TestCase(input = "3", expectedOutput = "3 x 1 = 3\n3 x 2 = 6\n3 x 3 = 9\n3 x 4 = 12\n3 x 5 = 15"),
                    TestCase(input = "7", expectedOutput = "7 x 1 = 7\n7 x 2 = 14\n7 x 3 = 21\n7 x 4 = 28\n7 x 5 = 35")
                )
            )
        )
    }

    fun getProblemById(id: Int): CodingProblem? {
        return problems.find { it.id == id }
    }
}
