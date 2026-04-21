@import gg.jte.Content

  @param Content content
  @param Content footer = null

  <!DOCTYPE html>
  <html lang="ru">
  <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Анализатор страниц</title>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  </head>
  <body class="d-flex flex-column min-vh-100">
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
          <div class="container-fluid">
              <a class="navbar-brand" href="/">Анализатор страниц</a>
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                  <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse" id="navbarNav">
                  <ul class="navbar-nav">
                      <li class="nav-item">
                          <a class="nav-link" href="/">Главная</a>
                      </li>
                      <li class="nav-item">
                          <a class="nav-link" href="/urls">Сайты</a>
                      </li>
                  </ul>
              </div>
          </div>
      </nav>

      <main class="flex-grow-1">
          <div class="container-lg mt-5">
              ${content}
          </div>
      </main>

      <footer class="footer mt-auto py-3 bg-light">
          <div class="container-lg">
              <span class="text-muted">
                  @if(footer != null)
                      ${footer}
                  @else
                      created by <a href="https://github.com/nodirbek9" target="_blank">Nodirbek</a>
                  @endif
              </span>
          </div>
      </footer>

      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  </body>
  </html>