const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  
  // Configuration du proxy pour le développement
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8111',
        changeOrigin: true,
        logLevel: 'debug'
      }
    }
  },
  
  // Génération de fichiers avec hachage dans les noms pour la mise en cache navigateur
  filenameHashing: true,
  
  // Autres configurations
  productionSourceMap: false,
  
  // Configuration avancée de webpack
  configureWebpack: {
    // Ajouter des plugins webpack ici si nécessaire
    optimization: {
      splitChunks: {
        chunks: 'all'
      }
    }
  },
  
  // Options pour les chargeurs de fichiers CSS/SASS
  css: {
    loaderOptions: {
      sass: {
        additionalData: `@import "@/assets/scss/variables.scss";`
      }
    }
  }
}); 