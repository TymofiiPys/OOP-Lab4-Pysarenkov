const getHeaderConfig = () => {
    const token = window.localStorage.getItem("Token");

    return {
        headers: {
            'Content-Type': 'application/json',
            'access-token': token
        }
    };
}

export default getHeaderConfig;