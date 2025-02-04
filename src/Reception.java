import java.sql.*;
import java.util.Scanner;

public class Reception {
    private static final String url = "jdbc:mysql://localhost:3306/hotelDb";
    private static final String user = "root";
    private static final String password = "Yash@15082005";

    public static void main(String[] args) throws ClassNotFoundException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection conn = DriverManager.getConnection(url,user,password);
            Scanner sc = new Scanner(System.in);

            while(true){
                System.out.println("1 . Register New User");
                System.out.println("2 . View Reservations");
                System.out.println("3 . Get Room No of a Customer");
                System.out.println("4 . Update Reservation");
                System.out.println("5 . Delete Reservation");
                System.out.println("0 . to Exit ");
                System.out.print("Enter Your Choice : ");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice){
                    case 1:
                        insertion(conn,sc);
                        break;
                    case 2:
                        reading(conn);
                        break;
                    case 3:
                        querying(conn,sc);
                        break;
                    case 4:
                        update(conn,sc);
                        break;
                    case 5:
                        delete(conn,sc);
                        break;
                    case 0:
                        exit();
                        return;
                }
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertion(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter Guest Name : ");
        String guest_name = sc.nextLine();
        System.out.print("Enter Room Number : ");
        int room_no = sc.nextInt();
        System.out.print("Enter Contact Info : ");
        String contact = sc.next();
        String query = "INSERT INTO reservations(guest_name,room_no,contact_no) "+
                "VALUES('"+guest_name+"',"+room_no+",'"+contact+"')";
        Statement stmt = conn.createStatement();

        int affected_rows = stmt.executeUpdate(query);
        if(affected_rows > 0){
            System.out.println("Detail Entered Successfully");
        }
        else{
            System.out.println("No rows were affected. Check your query or data.");
        }
    }

    private static void reading(Connection conn){
        String query = "Select * from reservations";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.printf("%-15s %-20s %-10s %-15s %-30s%n",
                    "Reservation ID", "Guest Name", "Room No", "Contact No", "Reservation Date");
            System.out.println("--------------------------------------------------------------------------");

            while(rs.next()){
                int reg_id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int room_no = rs.getInt("room_no");
                String contact = rs.getString("contact_no");
                String reservation_date = rs.getTimestamp("reservation_date").toString();
                System.out.printf("%-15d %-20s %-10d %-15s %-30s%n", reg_id, name, room_no, contact, reservation_date);
            }
            System.out.println();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void querying(Connection conn , Scanner sc){
        System.out.print("Enter Reservation Id : ");
        int reg_no = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Customer Name : ");
        String name = sc.nextLine();
        String query = "SELECT room_no FROM reservations " +
                "WHERE reservation_id = " + reg_no +
                " AND guest_name = '" + name + "'";
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                int room_no = rs.getInt("room_no");
                System.out.println("The Room no for Registration id : "+reg_no+
                        " and Name "+name+" is "+room_no);
            }
            else{
                System.out.println("Reservation not found for the registration id and name");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void update(Connection conn , Scanner sc){
        System.out.print("Enter the Reservation id : ");
        int reserv_id = sc.nextInt();
        sc.nextLine();
        if(!reservationExists(conn,reserv_id)){
            System.out.println("Record does not exist .");
            return ;
        }
        System.out.print("Enter Guest Name : ");
        String name = sc.nextLine();
        System.out.print("Enter Room No : ");
        int room_no = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Contact Info :");
        String contact = sc.nextLine();
        String query = "UPDATE reservations SET guest_name = '"+name+"', room_no = "+room_no+
                " , contact_no = '"+contact+"' WHERE reservation_id = "+reserv_id+";";

        try{
            Statement stmt = conn.createStatement();
            int affected_rows = stmt.executeUpdate(query);
            if(affected_rows > 0){
                System.out.println("Details Updated Successfully .");
            }
            else{
                System.out.println("Updation Failed");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void delete(Connection conn , Scanner sc){
        System.out.print("Enter the Reservation id : ");
        int reserv_id = sc.nextInt();
        if(!reservationExists(conn,reserv_id)){
            System.out.println("There is no room reserved under this id");
        }
        else{
            String query = "DELETE from reservations "+
                    "WHERE reservation_id = "+reserv_id;
            try{
                Statement stmt = conn.createStatement();
                int affected_rows = stmt.executeUpdate(query);
                if(affected_rows > 0){
                    System.out.println("Record Deleted Successfully");
                }
                else{
                    System.out.println("Reservation deletion failed.");
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private static boolean reservationExists(Connection conn, int res_id){
        String query = "SELECT reservation_id FROM reservations where reservation_id = "+res_id;
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)){

            return rs.next();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void exit() throws InterruptedException {
        System.out.print("Exiting System ");
        int dots = 6;
        while(dots!=0){
            System.out.print(".");
            Thread.sleep(500);
            dots --;
        }
        System.out.println();
        System.out.println("Thanks for using our System .");
    }
}
