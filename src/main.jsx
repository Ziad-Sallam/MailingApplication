import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import Login from './Pages/Login.jsx'
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import Register from "./Pages/register.jsx";
import MailBox from "./Components/MailBox.jsx";
import ListBox from "./Components/ListBox.jsx";
import MailView from "./Components/MailView.jsx";

const router  = createBrowserRouter([
    {
        path: '/:user',
        element: <App />,
        children: [
            {
                path: '/:user/send',
                element: <MailBox />,
            },
            {
                path: '/:user/inbox',
                element: <ListBox />,
            },
            {
                path: '/:user/:mailID',
                element: <MailView />,
            }
        ]
    },
    {
        path: '/login',
        element: <Login />,
    },

    {
        path: '/register',
        element: <Register />,
    },

    {
        path: '*',
        element: <h1>Error 404</h1>
    }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <RouterProvider router={router}/>
  </StrictMode>,
)
