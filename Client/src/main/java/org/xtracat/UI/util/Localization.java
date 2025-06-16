package org.xtracat.UI.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Localization {
    private static Locale currentLocale;

    private static final Map<Locale, Map<String, String>> messages = new HashMap<>();
    private static final Map<Locale, DateTimeFormatter> dateFormators = new HashMap<>();


    static {

        dateFormators.put(new Locale("ru"),DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss zzzz", new Locale("ru")));
        dateFormators.put(new Locale("pt"), DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm:ss zzzz", new Locale("pt")));
        dateFormators.put(new Locale("el"), DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss zzzz", new Locale("el")));
        dateFormators.put(new Locale("es"),DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, HH:mm:ss zzzz", new Locale("es")));


        Map<String, String> ruMap = new HashMap<>();
        ruMap.put("lang", "Язык:");
        ruMap.put("sort", "Сортировка");
        ruMap.put("filter", "Фильтр");
        ruMap.put("create", "Создать");
        ruMap.put("edit", "Редактировать");
        ruMap.put("delete", "Удалить");
        ruMap.put("visualize", "Визуализировать");
        ruMap.put("help", "Помощь");
        ruMap.put("clear", "Очистить");
        ruMap.put("exit","Выйти");
        ruMap.put("user","Пользователь");
        ruMap.put("execute_script", "Выполнить из файла");
        ruMap.put("count_less_than_genre", "Подсчитать меньше жанра");
        ruMap.put("remove_lower_key", "Удалить меньший ключ");
        ruMap.put("count_greater_than_participants","Счёт больше кол-ва участников");
        ruMap.put("print_list_of_participants","Вывести список кол-ва участников");
        ruMap.put("id", "id");
        ruMap.put("name", "название");
        ruMap.put("coord_x", "координата X");
        ruMap.put("coord_y", "координата Y");
        ruMap.put("creation", "дата создания");
        ruMap.put("creation_date", "Дата создания коллекции:");
        ruMap.put("filter_by", "Фильтровать по:");
        ruMap.put("value", " Значение=");
        ruMap.put("genre", "жанр");
        ruMap.put("auth_title","RegForm");
        ruMap.put("edit_window", "Редактировать");
        ruMap.put("create_window", "Создание MusicBand");
        ruMap.put("password", "Пароль:");
        ruMap.put("login", "Войти");
        ruMap.put("username", "Имя:");
        ruMap.put("register", "Зарегистрироваться");
        ruMap.put("language", "Язык:");
        ruMap.put("login_or_sign_up", "Вход/Регистрация");
        ruMap.put("genre_noun", "Жанр:");
        ruMap.put("key_noun", "Ключ:");
        ruMap.put("accept", "Принять");
        ruMap.put("message", "Сообщение");
        ruMap.put("collection", "Коллекция");
        ruMap.put("new_element", "Новый элемент");
        ruMap.put("visualization", "Визуализация");
        ruMap.put("auth", "Авторизация");
        ruMap.put("close", "Закрыть");
        ruMap.put("script_result", "Результат скрипта");
        ruMap.put("select_element", "Выберите элемент");
        ruMap.put("element_removed", "Элемент удален");
        ruMap.put("empty_credentials_error","Логин или пароль не могут быть пустыми!");
        ruMap.put("main_title","MainForm");
        ruMap.put("add","Создать");
        ruMap.put("filter_value","Введите значение");
        ruMap.put("print_ascending_number_of_participants","Вывести список кол-ва участников");
        ruMap.put("count_greater_than_number_of_participants","Счёт больше кол-ва участников");
        ruMap.put("date","Дата создания коллекции");
        ruMap.put("help_title","HelpForm");
        //ruMap.put("edit_window","EditForm");
        ruMap.put("help_content","Выйти - Завершает сеанс текущего пользователя и возвращает в окно авторизации.\n" +
                "Создать - Открывает форму для добавления нового элемента в коллекцию.\n" +
                "Редактировать - Позволяет изменить данные выделенного в таблице элемента.\n" +
                "Удалить - Удаляет выбранный элемент из коллекции.\n" +
                "Очистить - Удаляет все элементы коллекции, принадлежащие текущему пользователю.\n" +
                "Выполнить из файла - Исполняет скрипт с командами из указанного файла.\n" +
                "Визуализация - Открывает область, где объекты коллекции представлены в графическом виде.\n" +
                "Вывести список кол-ва участников - Отображает список значений количества участников для всех элементов в порядке возрастания.\n" +
                "Счёт больше кол-ва участников - Подсчитывает количество элементов, у которых число участников больше заданного вами значения.\n" +
                "Помощь - Открывает это справочное окно.\n");
        messages.put(new Locale("ru"), ruMap);


        Map<String, String> ptMap = new HashMap<>();
        ptMap.put("lang", "Idioma:");
        ptMap.put("sort", "Ordenar");
        ptMap.put("filter", "Filtro");
        ptMap.put("create", "Criar");
        ptMap.put("edit", "Editar");
        ptMap.put("delete", "Excluir");
        ptMap.put("visualize", "Visualizar");
        ptMap.put("help", "Ajuda");
        ptMap.put("clear", "Limpar");
        ptMap.put("exit", "Sair");
        ptMap.put("user", "Usuário");
        ptMap.put("execute_script", "Executar de arquivo");
        ptMap.put("count_less_than_genre", "Contar < gênero");
        ptMap.put("remove_lower_key", "Remover chave menor");
        ptMap.put("count_greater_than_participants", "Contar > participantes");
        ptMap.put("print_list_of_participants", "Listar participantes");
        ptMap.put("id", "id");
        ptMap.put("name", "nome");
        ptMap.put("coord_x", "coordenada X");
        ptMap.put("coord_y", "coordenada Y");
        ptMap.put("creation", "data de criação");
        ptMap.put("creation_date", "Data de criação:");
        ptMap.put("filter_by", "Filtrar por:");
        ptMap.put("value", " Valor=");
        ptMap.put("genre", "gênero");
        ptMap.put("auth_title", "Formulário de Registo");
        ptMap.put("edit_window", "Editar Elemento");
        ptMap.put("create_window", "Criar Filme");
        ptMap.put("password", "Senha:");
        ptMap.put("login", "Entrar");
        ptMap.put("username", "Nome:");
        ptMap.put("register", "Registrar");
        ptMap.put("language", "Idioma:");
        ptMap.put("login_or_sign_up", "Entrar/Registrar");
        ptMap.put("genre_noun", "Gênero:");
        ptMap.put("key_noun", "Chave:");
        ptMap.put("accept", "Aceitar");
        ptMap.put("message", "Mensagem");
        ptMap.put("collection", "Coleção");
        ptMap.put("new_element", "Novo elemento");
        ptMap.put("visualization", "Visualização");
        ptMap.put("auth", "Autenticação");
        ptMap.put("close", "Fechar");
        ptMap.put("script_result", "Resultado do script");
        ptMap.put("select_element", "Selecione o elemento");
        ptMap.put("element_removed", "Elemento removido");
        ptMap.put("empty_credentials_error", "Usuário ou senha não podem estar vazios!");
        ptMap.put("add", "Adicionar");
        ptMap.put("filter_value", "Digite o valor");
        ptMap.put("print_ascending_number_of_participants", "Listar participantes (crescente)");
        ptMap.put("count_greater_than_number_of_participants", "Contar > participantes");
        ptMap.put("date", "Data de criação da coleção");
        ptMap.put("help_title","HelpForm");
        ptMap.put("help_content", "Sair - Encerra a sessão do usuário atual e retorna à janela de login.\n" +
                "Criar - Abre o formulário para adicionar um novo elemento à coleção.\n" +
                "Editar - Permite modificar os dados do elemento selecionado na tabela.\n" +
                "Excluir - Remove o elemento selecionado da coleção.\n" +
                "Limpar - Remove todos os elementos da coleção que pertencem ao usuário atual.\n" +
                "Executar do arquivo - Executa um script de comandos a partir do arquivo especificado.\n" +
                "Visualização - Abre uma área que visualiza os objetos da coleção graficamente.\n" +
                "Listar contagem de participantes - Exibe uma lista com as contagens de participantes de todos os elementos em ordem crescente.\n" +
                "Contar com mais participantes - Conta o número de elementos cujo número de participantes é maior que o valor especificado.\n" +
                "Ajuda - Abre esta janela de ajuda.\n");
        messages.put(new Locale("pt"), ptMap);


        Map<String, String> elMap = new HashMap<>();
        elMap.put("lang", "Γλώσσα:");
        elMap.put("sort", "Ταξινόμηση");
        elMap.put("filter", "Φίλτρο");
        elMap.put("create", "Δημιουργία");
        elMap.put("edit", "Επεξεργασία");
        elMap.put("delete", "Διαγραφή");
        elMap.put("visualize", "Οπτικοποίηση");
        elMap.put("help", "Βοήθεια");
        elMap.put("clear", "Εκκαθάριση");
        elMap.put("exit", "Έξοδος");
        elMap.put("user", "Χρήστης");
        elMap.put("execute_script", "Εκτέλεση από αρχείο");
        elMap.put("count_less_than_genre", "Μέτρηση < είδος");
        elMap.put("remove_lower_key", "Διαγραφή μικρότερου κλειδιού");
        elMap.put("count_greater_than_participants", "Μέτρηση > συμμετέχοντες");
        elMap.put("print_list_of_participants", "Λίστα συμμετεχόντων");
        elMap.put("id", "id");
        elMap.put("name", "όνομα");
        elMap.put("coord_x", "συντεταγμένη X");
        elMap.put("coord_y", "συντεταγμένη Y");
        elMap.put("creation", "ημερ. δημιουργίας");
        elMap.put("creation_date", "Ημερ. δημιουργίας:");
        elMap.put("filter_by", "Φίλτρο ανά:");
        elMap.put("value", " Τιμή=");
        elMap.put("genre", "είδος");
        elMap.put("auth_title", "Φόρμα Εγγραφής");
        elMap.put("edit_window", "Επεξεργασία Στοιχείου");
        elMap.put("create_window", "Δημιουργία Ταινίας");
        elMap.put("password", "Κωδικός:");
        elMap.put("login", "Είσοδος");
        elMap.put("username", "Όνομα:");
        elMap.put("register", "Εγγραφή");
        elMap.put("language", "Γλώσσα:");
        elMap.put("login_or_sign_up", "Είσοδος/Εγγραφή");
        elMap.put("genre_noun", "Είδος:");
        elMap.put("key_noun", "Κλειδί:");
        elMap.put("accept", "Αποδοχή");
        elMap.put("message", "Μήνυμα");
        elMap.put("collection", "Συλλογή");
        elMap.put("new_element", "Νέο στοιχείο");
        elMap.put("visualization", "Οπτικοποίηση");
        elMap.put("auth", "Εξουσιοδότηση");
        elMap.put("close", "Κλείσιμο");
        elMap.put("script_result", "Αποτέλεσμα script");
        elMap.put("select_element", "Επιλέξτε στοιχείο");
        elMap.put("element_removed", "Το στοιχείο διαγράφηκε");
        elMap.put("empty_credentials_error", "Το όνομα χρήστη ή ο κωδικός δεν μπορεί να είναι κενά!");
        elMap.put("add", "Προσθήκη");
        elMap.put("filter_value", "Εισαγάγετε τιμή");
        elMap.put("print_ascending_number_of_participants", "Λίστα συμμετεχόντων (αύξουσα)");
        elMap.put("count_greater_than_number_of_participants", "Μέτρηση > συμμετέχοντες");
        elMap.put("date", "Ημερομηνία δημιουργίας συλλογής");
        elMap.put("help_title","HelpForm");
        elMap.put("help_content", "Έξοδος - Τερματίζει τη συνεδρία του τρέχοντος χρήστη και επιστρέφει στο παράθυρο σύνδεσης.\n" +
                "Δημιουργία - Ανοίγει τη φόρμα για την προσθήκη ενός νέου στοιχείου στη συλλογή.\n" +
                "Επεξεργασία - Επιτρέπει την τροποποίηση των δεδομένων του επιλεγμένου στοιχείου στον πίνακα.\n" +
                "Διαγραφή - Διαγράφει το επιλεγμένο στοιχείο από τη συλλογή.\n" +
                "Εκκαθάριση - Διαγράφει όλα τα στοιχεία της συλλογής που ανήκουν στον τρέχοντα χρήστη.\n" +
                "Εκτέλεση από αρχείο - Εκτελεί ένα σενάριο εντολών από το καθορισμένο αρχείο.\n" +
                "Οπτικοποίηση - Ανοίγει μια περιοχή όπου τα αντικείμενα της συλλογής αναπαρίστανται γραφικά.\n" +
                "Εμφάνιση λίστας αριθμού συμμετεχόντων - Εμφανίζει μια λίστα με τις τιμές του αριθμού των συμμετεχόντων για όλα τα στοιχεία σε αύξουσα σειρά.\n" +
                "Μέτρηση περισσότερων συμμετεχόντων - Μετρά τον αριθμό των στοιχείων όπου ο αριθμός των συμμετεχόντων είναι μεγαλύτερος από μια καθορισμένη τιμή.\n" +
                "Βοήθεια - Ανοίγει αυτό το παράθυρο βοήθειας.\n");

        messages.put(new Locale("el"), elMap);


        Map<String, String> esMap = new HashMap<>();
        esMap.put("lang", "Idioma:");
        esMap.put("sort", "Ordenar");
        esMap.put("filter", "Filtro");
        esMap.put("create", "Crear");
        esMap.put("edit", "Editar");
        esMap.put("delete", "Eliminar");
        esMap.put("visualize", "Visualizar");
        esMap.put("help", "Ayuda");
        esMap.put("clear", "Limpiar");
        esMap.put("exit", "Salir");
        esMap.put("user", "Usuario");
        esMap.put("execute_script", "Ejecutar de archivo");
        esMap.put("count_less_than_genre", "Contar < género");
        esMap.put("remove_lower_key", "Eliminar clave menor");
        esMap.put("count_greater_than_participants", "Contar > participantes");
        esMap.put("print_list_of_participants", "Listar participantes");
        esMap.put("id", "id");
        esMap.put("name", "nombre");
        esMap.put("coord_x", "coordenada X");
        esMap.put("coord_y", "coordenada Y");
        esMap.put("creation", "fecha de creación");
        esMap.put("creation_date", "Fecha de creación:");
        esMap.put("filter_by", "Filtrar por:");
        esMap.put("value", " Valor=");
        esMap.put("genre", "género");
        esMap.put("auth_title", "Formulario de Registro");
        esMap.put("edit_window", "Editar Elemento");
        esMap.put("create_window", "Crear Película");
        esMap.put("password", "Contraseña:");
        esMap.put("login", "Ingresar");
        esMap.put("username", "Nombre:");
        esMap.put("register", "Registrar");
        esMap.put("language", "Idioma:");
        esMap.put("login_or_sign_up", "Ingresar/Registrar");
        esMap.put("genre_noun", "Género:");
        esMap.put("key_noun", "Clave:");
        esMap.put("accept", "Aceptar");
        esMap.put("message", "Mensaje");
        esMap.put("collection", "Colección");
        esMap.put("new_element", "Nuevo elemento");
        esMap.put("visualization", "Visualización");
        esMap.put("auth", "Autenticación");
        esMap.put("close", "Cerrar");
        esMap.put("script_result", "Resultado del script");
        esMap.put("select_element", "Seleccione el elemento");
        esMap.put("element_removed", "Elemento eliminado");
        esMap.put("empty_credentials_error", "¡Usuario o contraseña no pueden estar vacíos!");
        esMap.put("add", "Añadir");
        esMap.put("filter_value", "Ingrese el valor");
        esMap.put("print_ascending_number_of_participants", "Listar participantes (ascendente)");
        esMap.put("count_greater_than_number_of_participants", "Contar > participantes");
        esMap.put("date", "Fecha de creación de la colección");
        esMap.put("help_title","HelpForm");
        esMap.put("help_content", "Salir - Cierra la sesión del usuario actual y regresa a la ventana de inicio de sesión.\n" +
                "Crear - Abre el formulario para agregar un nuevo elemento a la colección.\n" +
                "Editar - Permite modificar los datos del elemento seleccionado en la tabla.\n" +
                "Eliminar - Elimina el elemento seleccionado de la colección.\n" +
                "Limpiar - Elimina todos los elementos de la colección que pertenecen al usuario actual.\n" +
                "Ejecutar desde archivo - Ejecuta un script de comandos desde el archivo especificado.\n" +
                "Visualización - Abre un área que visualiza los objetos de la colección de forma gráfica.\n" +
                "Listar cantidad de participantes - Muestra una lista con la cantidad de participantes de todos los elementos en orden ascendente.\n" +
                "Contar con más participantes - Cuenta el número de elementos donde la cantidad de participantes es mayor que el valor especificado.\n" +
                "Ayuda - Abre esta ventana de ayuda.\n");

        messages.put(new Locale("es"), esMap);
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
    }

    public static String get(String key) {
        return messages.getOrDefault(currentLocale, messages.get(new Locale("ru"))).getOrDefault(key, "!" + key + "!");
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static String getDate(ZonedDateTime dateTime){
        return dateTime.format(dateFormators.get(currentLocale));
    }

}
