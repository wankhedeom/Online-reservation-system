import java.io.*;
import java.util.*;

public class OnlineReservationSystem {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        if (login()) {
            while (true) {
                System.out.println("\n--- Online Reservation System ---");
                System.out.println("1. Reserve Ticket");
                System.out.println("2. Cancel Ticket");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        reserveTicket();
                        break;
                    case 2:
                        cancelTicket();
                        break;
                    case 3:
                        System.out.println("Thank you for using the system.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }

    // ---------- LOGIN ----------
    
public static boolean login() {
    System.out.print("Enter login ID: ");
    String username = sc.nextLine();
    System.out.print("Enter password: ");
    String password = sc.nextLine();

    File file = new File("users.txt");

    // ✅ DEBUG PRINT
    System.out.println("Looking for users.txt at: " + file.getAbsolutePath());
    System.out.println("File exists: " + file.exists());

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] creds = line.split(",");
            if (creds[0].equals(username) && creds[1].equals(password)) {
                System.out.println("Login successful!");
                return true;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading user file.");
    }

    System.out.println("Invalid login credentials.");
    return false;
}

    // ---------- RESERVATION ----------
    public static void reserveTicket() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reservations.txt", true))) {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            System.out.print("Enter age: ");
            int age = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter train number: ");
            String trainNo = sc.nextLine();
            String trainName = getTrainName(trainNo);
            System.out.print("Enter class type (e.g. Sleeper/AC): ");
            String classType = sc.nextLine();
            System.out.print("Enter date of journey (dd-mm-yyyy): ");
            String date = sc.nextLine();
            System.out.print("From (place): ");
            String from = sc.nextLine();
            System.out.print("To (destination): ");
            String to = sc.nextLine();

            String pnr = "PNR" + System.currentTimeMillis();

            String data = pnr + "," + name + "," + age + "," + trainNo + "," + trainName + "," + classType + "," + date + "," + from + "," + to;
            bw.write(data);
            bw.newLine();

            System.out.println("Ticket Reserved Successfully! Your PNR: " + pnr);
        } catch (IOException e) {
            System.out.println("Error saving reservation.");
        }
    }

    public static String getTrainName(String trainNo) {
        switch (trainNo) {
            case "121": return "Mumbai Express";
            case "122": return "Deccan Queen";
            case "123": return "Goa Superfast";
            default: return "Generic Express";
        }
    }

    // ---------- CANCELLATION ----------
    public static void cancelTicket() {
        System.out.print("Enter your PNR: ");
        String pnr = sc.nextLine();

        File inputFile = new File("reservations.txt");
        File tempFile = new File("temp.txt");
        boolean found = false;

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(pnr + ",")) {
                    found = true;
                    System.out.println("Booking Found:\n" + line.replace(",", " | "));
                    System.out.print("Confirm cancellation? (yes/no): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("yes")) {
                        System.out.println("Ticket cancelled successfully.");
                        continue;
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error processing cancellation.");
        }

        // Replace old file
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            System.out.println("Error updating reservation file.");
        }

        if (!found) {
            System.out.println("PNR not found.");
        }
    }
}
