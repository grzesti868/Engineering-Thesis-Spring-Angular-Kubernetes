#FROM nginx:alpine
#LABEL author=Grzegorz_Stich
#COPY ./dist/commerce-pwa ./usr/share/nginx/html
#EXPOSE 80

#TEST1

#TEST2
FROM nginx:alpine
LABEL author=Grzegorz_Stich
COPY ./dist/commerce-pwa ./usr/share/nginx/html
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY ./nginx.conf /etc/nginx/conf.d
EXPOSE 80

#Apache config
#FROM httpd:alpine
#LABEL author=Grzegorz_Stich
#COPY ./dist/commerce-pwa /usr/local/apache2/htdocs/