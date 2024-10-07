import chalk from 'chalk';

const msg = 'HELLO WORLD!';

console.log( 
`
${chalk.blue(msg)}
${chalk.inverse(msg)}
${chalk.blue.bgRed.bold(msg)}
${chalk.blue.underline.bold(msg)}
` 
);

const error = chalk.bold.red;
const warning = chalk.keyword('orange');
 
console.log(error('Error!'));
console.log(warning('Warning!'));

console.log(`
CPU: ${chalk.red('90%')}
RAM: ${chalk.green('40%')}
DISK: ${chalk.yellow('70%')}
`);
 