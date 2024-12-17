import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import Login from './Pages/Login.jsx'
import {createBrowserRouter, RouterProvider} from 'react-router-dom'
import Register from "./Pages/register.jsx";
import MailBox from "./Components/MailBox.jsx";
import ListBox from "./Components/ListBox.jsx";
import MailView from "./Components/MailView.jsx";
import ContactBox from './Components/ContactBox.jsx';
import MailBoxContact from './Components/MailBoxContact.jsx'


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
                path: '/:user/:folderName/:mailID',
                element: <MailView />,
            },

            {
                path: '/:user/folder/:folderName',
                element: <ListBox />,
            },
            {
                path: '/:user/draft/:id',
                element: <MailBox />,
            },
            {
                path: '/:user/contact',
                element: <ContactBox />,
            },
            {
                path: '/:user/sendContact/:ContactEmail',
                element: <MailBoxContact />,
            }
        ]
    },
    {
        path: '/',
        element: <Login />,
    },
    {
        path: '/register',
        element: <Register />,
    },

    {
        path: '*',
        element: <h1>Error 404 :)</h1>
    }
])

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <RouterProvider router={router}/>
  </StrictMode>,
)
