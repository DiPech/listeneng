const webpack = require("webpack");
const path = require("path");
const dotenv = require("dotenv");

const rootDir = path.resolve(__dirname, ".");
const javascriptDir = path.resolve(rootDir, "src/main/javascript");
const resourcesDir = path.resolve(rootDir, "src/main/resources");
const baseDir = path.resolve(resourcesDir, "public/static");
const outputDir = path.resolve(baseDir, "dist");

dotenv.config({path: path.resolve(rootDir, "conf/app.env")});

const VueLoaderPlugin = require("vue-loader/lib/plugin")

module.exports = {
    entry: path.resolve(javascriptDir, "index.js"),
    output: {
        path: outputDir,
        filename: "bundle.js"
    },
    performance: {
        hints: false
    },
    resolve: {
        extensions: [".js", ".vue", ".json", ".scss"],
        alias: {
            "vue": "vue/dist/vue.esm.js",
            "@": javascriptDir
        }
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: "vue-loader"
            },
            {
                test: /\.js$/,
                loader: "babel-loader"
            },
            {
                test: /\.css$/,
                use: [
                    "vue-style-loader",
                    "css-loader"
                ]
            },
            {
                test: /\.s[ac]ss$/i,
                use: [
                    "style-loader",
                    "css-loader",
                    "sass-loader",
                ],
            },
            {
                test: /\.(png|svg|jpg|gif|ico)$/,
                use: [
                    {
                        loader: "file-loader",
                        options: {
                            name: "[name].[ext]",
                            publicPath: "./dist/",
                        }
                    }
                ],
            }
        ]
    },
    plugins: [
        new VueLoaderPlugin(),
        new webpack.DefinePlugin({
            IS_DEV: JSON.stringify(process.env.WEBPACK_DEV_SERVER),
            APP_DOMAIN: JSON.stringify(process.env.APP_DOMAIN)
        })
    ],
    devServer: {
        host: process.env.APP_DOMAIN,
        disableHostCheck: true,
        port: 9000,
        open: true,
        liveReload: true,
        contentBase: baseDir,
        publicPath: "/dist/",
        index: "index.html"
    }
};
