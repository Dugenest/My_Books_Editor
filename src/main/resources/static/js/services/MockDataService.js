/**
 * Service de données fictives pour le mode développement
 * Permet de faire fonctionner l'application même en cas de problème avec le backend
 */

class MockDataService {
    // Catégories factices
    getCategories() {
        return {
            content: [
                { id: 1, name: "Roman", description: "Livres de fiction", icon: "book", color: "#4caf50" },
                { id: 2, name: "Science Fiction", description: "Livres de science-fiction", icon: "rocket", color: "#2196f3" },
                { id: 3, name: "Histoire", description: "Livres historiques", icon: "history", color: "#ff9800" },
                { id: 4, name: "Policier", description: "Romans policiers", icon: "search", color: "#f44336" },
                { id: 5, name: "Jeunesse", description: "Livres pour enfants", icon: "child", color: "#9c27b0" }
            ],
            totalElements: 5,
            totalPages: 1,
            size: 10,
            number: 0
        };
    }

    // Auteurs factices
    getAuthors(page = 0, size = 10) {
        const authors = [
            {
                id: 1,
                firstName: "Victor",
                lastName: "Hugo",
                nationality: "Français",
                biography: "Écrivain romantique français",
                birthDate: "1802-02-26"
            },
            {
                id: 2,
                firstName: "Charles",
                lastName: "Baudelaire",
                nationality: "Français",
                biography: "Poète français",
                birthDate: "1821-04-09"
            }
        ];

        // Simuler la pagination
        const startIndex = page * size;
        const endIndex = Math.min(startIndex + size, authors.length);
        const pagedAuthors = authors.slice(startIndex, endIndex);

        return {
            content: pagedAuthors,
            totalElements: authors.length,
            totalPages: Math.ceil(authors.length / size),
            size: size,
            number: page
        };
    }

    // Éditeurs factices
    getEditors(page = 0, size = 10) {
        const editors = [
            {
                id: 1,
                company: "Gallimard",
                description: "Éditeur français"
            },
            {
                id: 2,
                company: "Flammarion",
                description: "Éditeur français"
            }
        ];

        // Simuler la pagination
        const startIndex = page * size;
        const endIndex = Math.min(startIndex + size, editors.length);
        const pagedEditors = editors.slice(startIndex, endIndex);

        return {
            content: pagedEditors,
            totalElements: editors.length,
            totalPages: Math.ceil(editors.length / size),
            size: size,
            number: page
        };
    }

    // Livres factices
    getBooks(page = 0, size = 10) {
        const books = [
            {
                id: 1,
                title: "Les Misérables",
                ISBN: "9782253096337",
                price: 12.99,
                stock: 15,
                publishDate: "1862-01-01",
                detail: "Un chef-d'œuvre de la littérature française",
                author: { id: 1, firstName: "Victor", lastName: "Hugo" },
                editor: { id: 1, company: "Gallimard" }
            },
            {
                id: 2,
                title: "Le Tour du monde en 80 jours",
                ISBN: "9782070518784",
                price: 9.99,
                stock: 8,
                publishDate: "1873-01-30",
                detail: "Un roman d'aventures classique",
                author: { id: 2, firstName: "Jules", lastName: "Verne" },
                editor: { id: 2, company: "Hachette" }
            },
            {
                id: 3,
                title: "Le Crime de l'Orient-Express",
                ISBN: "9782253041931",
                price: 8.50,
                stock: 12,
                publishDate: "1934-01-01",
                detail: "Un des plus célèbres romans policiers",
                author: { id: 3, firstName: "Agatha", lastName: "Christie" },
                editor: { id: 3, company: "Flammarion" }
            },
            {
                id: 4,
                title: "Harry Potter à l'école des sorciers",
                ISBN: "9782070518425",
                price: 15.90,
                stock: 25,
                publishDate: "1997-06-26",
                detail: "Le premier tome de la saga Harry Potter",
                author: { id: 4, firstName: "J.K.", lastName: "Rowling" },
                editor: { id: 4, company: "Albin Michel" }
            },
            {
                id: 5,
                title: "Shining",
                ISBN: "9782253151579",
                price: 11.90,
                stock: 7,
                publishDate: "1977-01-28",
                detail: "Un thriller psychologique effrayant",
                author: { id: 5, firstName: "Stephen", lastName: "King" },
                editor: { id: 5, company: "Le Seuil" }
            }
        ];

        // Simuler la pagination
        const startIndex = page * size;
        const endIndex = Math.min(startIndex + size, books.length);
        const pagedBooks = books.slice(startIndex, endIndex);

        return {
            content: pagedBooks,
            totalElements: books.length,
            totalPages: Math.ceil(books.length / size),
            size: size,
            number: page
        };
    }
}

export default new MockDataService(); 