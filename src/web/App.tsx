import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AppFrame } from './AppFrame';
import { Home } from './page/Home';
import { About } from './page/About';

const routeConfig = [
    {
        path: '/',
        element: <AppFrame />,
        children: [
            { path: "", element: <Home /> },
            { path: "About", element: <About /> }
        ]
    }
];

const router = createBrowserRouter(routeConfig);

export const App = () => {
    return (
        <React.StrictMode>
            <React.Fragment>
                <RouterProvider router={router} />
            </React.Fragment>
        </React.StrictMode>
    );
}
