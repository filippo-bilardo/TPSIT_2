public class esempio1 {
    public static void main(String[] args) {
        String str1 = "Hello";
        String str2 = "World";
        String str3 = str1 + " " + str2; // Concatenazione di stringhe
        System.out.println(str3); // Output: Hello World
        
        String subStr = str3.substring(6); // Sottostringhe
        System.out.println(subStr); // Output: World
        
        String replaced = str3.replace("World", "Java");
        System.out.println(replaced); // Output: Hello Java
        
        String upper = str3.toUpperCase();
        System.out.println(upper); // Output: HELLO WORLD
    }
}
