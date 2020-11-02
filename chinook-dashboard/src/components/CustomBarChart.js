import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/styles';
import { Card,CardContent } from '@material-ui/core';
import { BarChart, XAxis, YAxis, Bar, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import {Typography} from '@material-ui/core';

const styles = theme => ({
  card: {
    minWidth: 275,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 2px',
    transform: 'scale(0.8)',
  },
  title: {
    marginBottom: 16,
    fontSize: 14,
  },
  pos: {
    marginBottom: 12,
  },
});

const CustomBarChart = (props) => {
  const { classes, data, labelKey, valueKey } = props;
  return (
    <Card raised className={classes.card}>
      <CardContent>
        <Typography className={classes.pos}>{props.heading}</Typography>
        <ResponsiveContainer width="100%" height={375}>
          <BarChart isAnimationActive data={data}>
            <XAxis dataKey={labelKey ? labelKey : 'name'} />
            <YAxis dataKey={valueKey ? valueKey : 'value'} />
            <Tooltip />
            <Legend />
            <Bar
              dataKey={valueKey ? valueKey : 'value'}
              legendType="circle"
              fill="#0088FE"
            />
          </BarChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};


CustomBarChart.propTypes = {
  heading: PropTypes.string.isRequired,
  data: PropTypes.arrayOf(PropTypes.shape({
    name: PropTypes.string,
    value: PropTypes.number,
  })),
  classes: PropTypes.objectOf(PropTypes.string).isRequired,
  labelKey: PropTypes.string,
  valueKey: PropTypes.string,
};

CustomBarChart.defaultProps = {
  labelKey: undefined,
  valueKey: undefined,
  data: [],
};

export default withStyles(styles)(CustomBarChart);
