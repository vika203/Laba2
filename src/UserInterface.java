package lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserInterface extends JFrame {
    private JTabbedPane tabbedPane;
    private JMenuBar menuBar;
    private JMenuItem allProducts;
    private JMenu menu;
    private JMenuItem deleteProduct;
    private JMenuItem deleteGroupOfProducts;

    private JMenuItem addProduct;
    private JMenuItem addGroupOfProducts;

    private JMenuItem editProduct;
    private JMenuItem editGroupOfProducts;
    private JMenuItem lookFor;


    //заповнюємо масив прод і непрод СТРІЧОК

    Products allGroup = new Products();
    ProductGroup neProd = new ProductGroup("C:\\Users\\vika\\Desktop\\Shopping\\NeProd", "Класні товари", "Непродовольчі товари");
    ProductGroup prod = new ProductGroup("C:\\Users\\vika\\Desktop\\Shopping\\Prod", "Хороші товари", "Продовольчі товари");

    UserInterface() throws IOException {
        super("Головна сторінка");
        super.setBounds(200, 200, 984, 576);
        super.setLayout(new BorderLayout()); // Зміна FlowLayout на BorderLayout
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
/*****
 В цій чатисні коду ми ініцілазізуємо масиви даними з файлів початковими значеннями, групи продовольчі, непродовольчі
 *****/
        BufferedReader readerNeprod = new BufferedReader(new FileReader("C:\\Users\\vika\\Desktop\\Shopping\\NeProd\\NeProd.txt"));
        String str = "";
        while ((str = readerNeprod.readLine()) != null) {
            neProd.addProductToGroup(new Product(str.split(";")));
        }
        readerNeprod.close();

        BufferedReader readerProd = new BufferedReader(new FileReader("C:\\Users\\vika\\Desktop\\Shopping\\Prod\\Prod.txt"));
        str = "";
        while ((str = readerProd.readLine()) != null) {
            prod.addProductToGroup(new Product(str.trim().split(";")));
        }

        readerProd.close();


        allGroup.addGroupToAllGroup(neProd);
        //   allGroup.addGroupToAllGroup(prod);


        menuBar = new JMenuBar();
        menu = new JMenu("Меню");

        allProducts = new JMenuItem("Всі товари");
        deleteProduct = new JMenuItem("Видалити товар");
        deleteGroupOfProducts = new JMenuItem("Видалити групу товарів");
        addProduct = new JMenuItem("Додати товар");
        addGroupOfProducts = new JMenuItem("Додати групу товарів");
        editProduct = new JMenuItem("Редагувати товар");
        editGroupOfProducts = new JMenuItem("Редагувати групу товарів");
        lookFor = new JMenuItem("Пошук товару");

        menu.add(allProducts);
        menu.add(deleteProduct);
        menu.add(deleteGroupOfProducts);
        menu.add(addProduct);
        menu.add(addGroupOfProducts);
        menu.add(editProduct);
        menu.add(editGroupOfProducts);
        menu.add(lookFor);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER); // Додавання JTabbedPane до вмісту

        // Ініціалізація слухача подій для меню
        MenuListener menuListener = new MenuListener();
        allProducts.addActionListener(menuListener);
        deleteProduct.addActionListener(menuListener);
        deleteGroupOfProducts.addActionListener(menuListener);
        addProduct.addActionListener(menuListener);
        addGroupOfProducts.addActionListener(menuListener);
        editProduct.addActionListener(menuListener);
        editGroupOfProducts.addActionListener(menuListener);
        lookFor.addActionListener(menuListener);

        // Додавання слухача подій миші для вкладок (подвійне клацання == закриття вкладки)
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Перевірка подвійного кліку
                    int tabIndex = tabbedPane.getSelectedIndex();
                    if (tabIndex != -1) {
                        closeTab(tabIndex);
                    }
                }
            }
        });

        setVisible(true);
    }

    class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == allProducts) {
                if (tabbedPane.indexOfTab(allProducts.getText()) == -1) { // перевірка наявності вкладки
                    tabbedPane.addTab(allProducts.getText(), new JPanel());
                }

//видалити продукт з якоїсь групи
            } else if (e.getSource() == deleteProduct) {
                if (tabbedPane.indexOfTab(deleteProduct.getText()) == -1) {
                    tabbedPane.addTab(deleteProduct.getText(), new JPanel());
                }

//метод для видалення групи продуктів
            } else if (e.getSource() == deleteGroupOfProducts) {
                if (tabbedPane.indexOfTab(deleteGroupOfProducts.getText()) == -1) {
                    tabbedPane.addTab(deleteGroupOfProducts.getText(), new JPanel());
                }

//додавання продукту в якусь групу
            } else if (e.getSource() == addProduct) {
                String tabTitle = lookFor.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);
                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку і додаємо на неї текстове поле
                    JPanel newPanel = new JPanel();
                    JButton showAllGroupsbtn = new JButton("Показати всі групи: ");
                    showAllGroupsbtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource() == showAllGroupsbtn) {
                                for (ProductGroup productGroup : allGroup.getAllGroupArray()) {
                                    JLabel label = new JLabel(String.valueOf(productGroup));
                                    newPanel.add(label);

                                }
                            }
                        }
                    });

                    //JTextField textField = new JTextField(20); // Створення текстового поля
                    //newPanel.add(textField); // Додавання текстового поля на нову панель
                    tabbedPane.addTab(tabTitle, newPanel); // Додавання нової вкладки разом з панеллю
/*
                    // Логіка з пошуком товару
                    textField.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            String productName = textField.getText();
                            Product foundProduct = allGroup.findProductByName(productName);
                            if (foundProduct != null) {
                                // Створення та налаштування вмісту вкладки знайденого товару
                                JPanel productPanel = new JPanel();
                                JLabel productInfoLabel = new JLabel(String.valueOf(foundProduct));

                                // Додавання тексту з інформацією про продукт
                                productPanel.add(productInfoLabel);
                                // Додавання панелі товару на вкладку
                                tabbedPane.add(tabTitle, productPanel);
                                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1); // Перехід на нову вкладку
                            } else {
                                JOptionPane.showMessageDialog(null, "Товар не знайдено.", "Помилка", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    */
                }

//додавання групи до усіх груп продуктів
            } else if (e.getSource() == addGroupOfProducts) {
                if (tabbedPane.indexOfTab(addGroupOfProducts.getText()) == -1) {
                    tabbedPane.addTab(addGroupOfProducts.getText(), new JPanel());
                }

//редагування інформації про продукт
            } else if (e.getSource() == editProduct) {
                if (tabbedPane.indexOfTab(editProduct.getText()) == -1) {
                    tabbedPane.addTab(editProduct.getText(), new JPanel());
                }


//редагування групи товарів (назва і опис)
            } else if (e.getSource() == editGroupOfProducts) {
                String tabTitle = editGroupOfProducts.getText(); // Використовуєм editGroupOfProducts.getText() для отримання тексту кнопки
                int tabIndex = tabbedPane.indexOfTab(tabTitle);
                if (tabIndex == -1) {
                    JPanel newPanel = new JPanel();
                    newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));

                    JScrollPane scrollPane = new JScrollPane(newPanel);
                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    // Додавання скролл-панелі замість простої панелі до вкладки
                    tabbedPane.addTab(tabTitle, scrollPane);

                }


//пошук якогось продукту
            } else if (e.getSource() == lookFor) {
                String tabTitle = lookFor.getText();
                int tabIndex = tabbedPane.indexOfTab(tabTitle);
                if (tabIndex == -1) {
                    // Вкладка ще не існує, створюємо нову вкладку і додаємо на неї текстове поле
                    JPanel newPanel = new JPanel();
                    JLabel label = new JLabel("Введіть назву товару");
                    JTextField textField = new JTextField(20); // Створення текстового поля
                    newPanel.add(label);
                    newPanel.add(textField); // Додавання текстового поля на нову панель
                    tabbedPane.addTab(tabTitle, newPanel); // Додавання нової вкладки разом з панеллю

                    // Логіка з пошуком товару
                    textField.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            String productName = textField.getText();
                            Product foundProduct = allGroup.findProductByName(productName);
                            if (foundProduct != null) {
                                // Створення та налаштування вмісту вкладки знайденого товару
                                JPanel productPanel = new JPanel();
                                JLabel productInfoLabel = new JLabel(String.valueOf(foundProduct));

                                // Додавання тексту з інформацією про продукт
                                productPanel.add(productInfoLabel);
                                // Додавання панелі товару на вкладку
                                tabbedPane.add(tabTitle, productPanel);
                                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1); // Перехід на нову вкладку
                            } else {
                                JOptionPane.showMessageDialog(null, "Товар не знайдено.", "Помилка", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                }
            }
        }
    }

    public void closeTab(int tabIndex) {
        tabbedPane.remove(tabIndex);

    }
}
