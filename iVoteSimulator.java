// Daniel Cho

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class iVoteSimulator {

	class Student {
		private Integer studentID;
		private List<String> answer;
		private Integer score;

		public Student(Integer studentID, List<String> answer) {
			this.studentID = studentID;
			this.answer = answer;
		}

		public Integer getStudentID() {
			return studentID;
		}

		public Integer getScore() {
			return score;
		}

		public List<String> getAnswer() {
			return answer;
		}

		public void setStudentID(Integer studentID) {
			this.studentID = studentID;
		}

		public void setAnswer(List<String> answer) {
			this.answer = answer;
		}

		public void setScore(Integer score) {
			this.score = score;
		}
	}

	class Questions {
		private List<Integer> qType;
		private List<String> cAnswer;

		public Questions(List<Integer> qType, List<String> cAnswer) {

			this.qType = qType;
			this.cAnswer = cAnswer;
		}

		public List<Integer> getqType() {
			return qType;
		}

		public List<String> getcAnswer() {
			return cAnswer;
		}

		public void setType(List<Integer> qType) {
			this.qType = qType;
		}

		public void setAnswer(List<String> cAnswer) {
			this.cAnswer = cAnswer;
		}

	}

	class votingService {
		private List<Student> students;
		private HashMap<Integer, Map<String, Integer>> stats = new HashMap<Integer, Map<String, Integer>>();
		private Questions question;

		public votingService(Questions question, List<Student> students) {
			this.students = students;
			this.question = question;
		}

		public void setQuestion(Questions question) {
			this.question = question;
		}

		public void grades(Questions question, Student student) {
			int x = 0;
			List<String> ans = student.getAnswer();
			List<String> cans = question.getcAnswer();

			for (int i = 0; i < ans.size(); i++) {
				if (ans.get(i).equals(cans.get(i))) {
					x++;
				}
			}

			student.setScore(x);
			System.out.println("Grade: " + student.getScore() + " out of 5");
		}

		public void collectStatistics(List<Student> studentsList) {
			List<Integer> qTypes = question.getqType();
			for (int i = 0; i < qTypes.size(); i++) {
				int questionNumber = i + 1;
				Map<String, Integer> questionStats = new HashMap<>();

				for (Student student : studentsList) {
					List<String> studentAnswers = student.getAnswer();
					String answer = studentAnswers.get(i);
					// Update the statistics for the specified question and answer
					questionStats.put(answer, questionStats.getOrDefault(answer, 0) + 1);
				}

				// Store the statistics for this question
				stats.put(questionNumber, questionStats);
			}
		}

		public void printStats() {
			List<Integer> qTypes = question.getqType();
			for (int i = 0; i < qTypes.size(); i++) {
				int qNumber = i + 1;
				Map<String, Integer> questionStats = stats.get(qNumber);
				System.out.println("\nQ" + qNumber + ":");
				for (Map.Entry<String, Integer> entry : questionStats.entrySet()) {
					System.out.println(entry.getKey() + ": " + entry.getValue() + " people");
				}
			}
		}
	}

	class simulateDriver {

		Random random = new Random();
		Questions question;
		List<Student> studentList = new ArrayList<>();

		public void run() {
			// question type
			List<Integer> qt = new ArrayList<>(5);
			// correct answer
			List<String> canL = new ArrayList<>(5);
			// generate random number of students with a limit of 50
			int studentNum = random.nextInt(50);
			// randomly generate 5 questions

			for (int j = 0; j < 5; j++) {
				// 0 for single, 1 for multiple
				int singleMultiple = random.nextInt(2);
				// add it to the array list
				qt.add(singleMultiple);
				String x = "";
				// when it is single answer
				if (singleMultiple == 0) {
					// Generate a random answer among 5 choices
					Integer can = random.nextInt(5);
					x = Integer.toString(can);
					// add it to the answer list
					canL.add(x);
				}
				// when it is multiple answer
				// Inside the loop that generates student answers
				if (qt.get(j) == 1) { // Check if it's a multiple-choice question
					int count = random.nextInt(5) + 1; // Ensure at least one answer
					StringBuilder y = new StringBuilder();

					for (int k = 0; k < count; k++) {
						int z = random.nextInt(5);
						y.append(Integer.toString(z));
					}

					x = y.toString();
					// add it to correct answer list
					canL.add(x);
				}

			}

			question = new Questions(qt, canL);
			votingService votes = new votingService(question, studentList);

			// Generate student ID, and randomly generate answers
			for (int i = 0; i < studentNum; i++) {
				int id = 123456 + i;
				List<String> tempans = new ArrayList<>();
				// randomly generate 5 answers for each student
				for (int j = 0; j < 5; j++) {
					int rCAnswer = random.nextInt(5) + 1;

					if (qt.get(j) == 0) {
						// student answer
						tempans.add(Integer.toString(rCAnswer));
					} else {
						int count = random.nextInt(5) + 1; // Ensure at least one answer
						StringBuilder y = new StringBuilder();

						// make sure there is no duplicate (ex, 22 33)
						Set<Integer> uniqueChoices = new HashSet<>();

						while (uniqueChoices.size() < count) {
							int choice = random.nextInt(5) + 1; // Generates numbers from 1 to 5
							uniqueChoices.add(choice);
						}

						for (int choice : uniqueChoices) {
							y.append(Integer.toString(choice));
						}

						tempans.add(y.toString());
					}
				}
				Student students = new Student(id, tempans);
				studentList.add(students);
				votes.grades(question, students);
			}

			votes.collectStatistics(studentList);
			votes.printStats();
		}
	}

	public static void main(String[] args) {
		iVoteSimulator simul = new iVoteSimulator();
		simulateDriver driver = simul.new simulateDriver();
		driver.run();

	}
}
