
const card = document.querySelector('.content');
const spinner = document.querySelector('#spinner');


function mapCards(movies){
  const html = movies.map(movie => {
    let title = movie.title || movie.name;
    let isMovieOrTv=(movie.title)?'movie':'tv';
    return `
      <div class="card" >
        <div class="frontWeb" style="background-image: url(//image.tmdb.org/t/p/original${movie.poster_path});"> 
          <p>${title}</p>
        </div>

        <div class="back">
          <div>
            <div class="release_date">${title} <span>(${getYearFromDate(movie.release_date)})</span></div>
            <div class="movie_gens">${getGenNames(movie.genre_ids)}</div>
            <div>⭐${movie.vote_average}</div>
            
            <p class="overview">${movie.overview}</p>
            <a target="_blank" href="https://www.themoviedb.org/${isMovieOrTv}/${movie.id}" class="button">Details</a>
          </div>
        </div>

      </div>
    `;
  }).join('');
  card.innerHTML= 
    `<h1 class="heading">Peliculas</h1>`;
  card.innerHTML+= html;
}


async function fetchMovies(urlEndpoint) {
  let data;
  try {
    const response = await fetch(urlEndpoint);
    data = await response.json();

    //return (data);
  } catch (error) {
    console.log(error);
  }
  // return data.data;
  return data.items || data.results;
}

(async () => {
  const movies = await fetchMovies(URL_API);
   spinner.setAttribute("hidden", "");
  mapCards(movies);
})();