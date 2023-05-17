import setCookieJWTToken from "./cookie.js";
import getCookieJWTToken from "./cookie.js";
import getConfigForAxios from "./cookie.js";

const inputLogin = document.getElementById("username");
const inputPassword = document.getElementById("password");

document.getElementById("entryBtn").addEventListener('click', e=>{

    axios.post('/api/auth/login', {
        email: "user1@mail.ru",
        password: 'Qwerty'
    })
    .then((response) => {
        let responseData;
        if(response.hasOwnProperty('data')) {
            responseData = response.data;
        }
        let jwtToken;
        if(responseData.hasOwnProperty('jwtToken')) {
            jwtToken = responseData.jwtToken;
            setCookieJWTToken(jwtToken);
        }
    })
    .then((jwtToken) => {


        if(response.data.hasOwnProperty('jwtToken')) {
            const token = getCookieJWTToken();
            const config = getConfigForAxios(token);
            axios.get('/moderator/main', config)
        } else {
            alert(response)
        }
        console.log(response);
    })
    .catch((error) => {
        console.log(error);
    });
});