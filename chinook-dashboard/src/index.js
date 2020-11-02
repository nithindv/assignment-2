/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import ReactDOM from "react-dom";
import { useQuery, QueryCache, ReactQueryCacheProvider } from "react-query";
import { Grid } from '@material-ui/core';
import CustomPieChart from './components/CustomPieChart'
import CustomBarChart from './components/CustomBarChart'
import StatBox from './components/StatBox'

const queryCache = new QueryCache();

export default function App() {
  return (
    <ReactQueryCacheProvider queryCache={queryCache}>
      <Example />
    </ReactQueryCacheProvider>
  );
}

function Example() {
  const { isLoading: isLoadingR, error: errorR, data: dataR } = useQuery("sales-by-country", () =>
    fetch(
      `/searcher/chinook/sales-by-country/_search`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "searchParams": {}
      })
    }
    ).then((res) => res.json())
  );

  const { isLoading: isLoadingG, error: errorG, data: dataG } = useQuery("sales-by-genre", () =>
    fetch(
      `/searcher/chinook/sales-by-genre/_search`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        "searchParams": {}
      })
    }
    ).then((res) => res.json())
  );

  const { isLoading: isLoadingS, error: errorS, data: dataS } = useQuery("stats", () =>
  fetch(
    `/searcher/chinook/stats/_search`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      "searchParams": {}
    })
  }
  ).then((res) => res.json())
);

  if (isLoadingR || isLoadingG || isLoadingS) return "Loading...";

  if (errorR || errorG || errorS) return "An error has occurred: " + errorR.message + errorG.message;

  return (
    <div>

      <Grid container spacing={10}>

        <Grid item lg={2} sm={12} xs={12}>
          <StatBox value={dataS[0].total_sales} heading="Total Sales in $" />
          <StatBox value={dataS[0].total_quantity} heading="Quantity Sold" />
          <StatBox value={dataS[0].cust_count} heading="Total Customers" />
          <StatBox value={dataS[0].emp_count} heading="Employees" />
        </Grid>

      <Grid item lg={4} sm={12} xs={12}>
        <CustomBarChart
          data={dataR.slice(0, 10)}
          heading="Top 10 Performing Countries - Sales"
          labelKey="billing_country"
          valueKey="sum"
        />
      </Grid>

      <Grid item lg={3} sm={12} xs={12}>
        <CustomPieChart
          data={dataG}
          heading="Popularity by Genre"
          labelKey="name"
          valueKey="sum_qty"
        />
      </Grid>
      </Grid>
      
      {/* <ReactQueryDevtools initialIsOpen /> */}
    </div>
  );
}

const rootElement = document.getElementById("root");
ReactDOM.render(<App />, rootElement);
